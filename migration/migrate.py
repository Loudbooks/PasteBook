import os
import boto3
import psycopg2
from pymongo import MongoClient

MONGO_URI = os.environ.get("MONGO_URI", "mongodb://mongo:27017")
MONGO_DB = os.environ.get("MONGO_DB", "pastebook")
POSTGRES_DB = os.environ.get("POSTGRES_DB", "pastebook")
POSTGRES_USER = os.environ.get("POSTGRES_USER", "pastebook")
POSTGRES_PASSWORD = os.environ.get("POSTGRES_PASSWORD", "pastebook")
POSTGRES_HOST = os.environ.get("POSTGRES_HOST", "postgres")
POSTGRES_PORT = os.environ.get("POSTGRES_PORT", 5432)
S3_ACCESS_KEY = os.environ.get("S3_ACCESS_KEY_ID", "minioadmin")
S3_SECRET_KEY = os.environ.get("S3_SECRET_ACCESS_KEY", "minioadmin")
S3_ENDPOINT = os.environ.get("S3_ENDPOINT", "http://minio:9000")
S3_BUCKET = os.environ.get("S3_BUCKET", "pastebook")

mongo = MongoClient(MONGO_URI)
db = mongo[MONGO_DB]
users_col = db["users"]
pastes_col = db["pastes"]

pg_conn = psycopg2.connect(
    dbname=POSTGRES_DB,
    user=POSTGRES_USER,
    password=POSTGRES_PASSWORD,
    host=POSTGRES_HOST,
    port=POSTGRES_PORT
)
pg_cur = pg_conn.cursor()

s3 = boto3.client(
    's3',
    endpoint_url=S3_ENDPOINT,
    aws_access_key_id=S3_ACCESS_KEY,
    aws_secret_access_key=S3_SECRET_KEY
)

def migrate_user(user_doc):
    ip = user_doc.get("ip")
    if not ip:
        return
    pg_cur.execute("SELECT 1 FROM user_metadata WHERE ip = %s", (ip,))
    if pg_cur.fetchone():
        return
    pg_cur.execute(
        "INSERT INTO user_metadata (ip, id, requests, created_at, banned) VALUES (%s, %s, %s, %s, %s)",
        (
            ip,
            user_doc.get("id", "unknown"),
            user_doc.get("requests", 0),
            user_doc.get("created_at", 0),
            user_doc.get("banned", False),
        )
    )
    pg_conn.commit()

def migrate_paste(paste_doc):
    paste_id = str(paste_doc["_id"])
    pg_cur.execute("SELECT 1 FROM paste_metadata WHERE id = %s", (paste_id,))
    if pg_cur.fetchone():
        return
    pg_cur.execute(
        "INSERT INTO paste_metadata (id, title, created, report_book, wrap, creator_ip, expires_at) VALUES (%s, %s, %s, %s, %s, %s, %s)",
        (
            paste_id,
            paste_doc.get("title", ""),
            paste_doc.get("created", 0),
            paste_doc.get("report_book", False),
            paste_doc.get("wrap", False),
            paste_doc.get("creator_ip", ""),
            paste_doc.get("expires_at", 0),
        )
    )
    try:
        response = s3.get_object(Bucket=S3_BUCKET, Key=f"{paste_id}")
        content = response['Body'].read().decode('utf-8')
    except Exception:
        content = paste_doc.get("content", "")
    pg_cur.execute(
        "INSERT INTO paste_content (id, content) VALUES (%s, %s)",
        (paste_id, content)
    )
    pg_conn.commit()

def run_migration():
    for user_doc in users_col.find():
        migrate_user(user_doc)
    for paste_doc in pastes_col.find():
        migrate_paste(paste_doc)

if __name__ == "__main__":
    run_migration()
