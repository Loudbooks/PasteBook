use std::sync::Arc;
use std::time::Duration;
use chrono::Utc;
use futures_util::StreamExt;
use tokio::spawn;
use tokio::time::interval;
use log::{error, warn};
use crate::database::aws_service::AWSService;
use crate::database::mongodb_service::MongoService;

pub struct DeleteHandler {
    aws_service: Arc<AWSService>,
    mongo_service: Arc<MongoService>,
}

impl DeleteHandler {
    pub fn new(aws_service: Arc<AWSService>, mongo_service: Arc<MongoService>) -> Self {
        let handler = Self {
            aws_service,
            mongo_service,
        };

        handler.start_delete_loop();
        handler
    }

    pub(crate) fn start_delete_loop(&self) {
        let aws_service = Arc::clone(&self.aws_service);
        let mongo_service = Arc::clone(&self.mongo_service);

        spawn(async move {
            let mut interval = interval(Duration::from_secs(600));
            loop {
                interval.tick().await;
                if let Err(err) = Self::delete_files(&aws_service, &mongo_service).await {
                    error!("Error during deletion process: {}", err);
                }
            }
        });
    }

    async fn delete_files(
        aws_service: &Arc<AWSService>,
        mongo_service: &Arc<MongoService>,
    ) -> Result<(), String> {
        let now = Utc::now().timestamp_millis();
        let mut deletable_pastes = Vec::new();
        let mut all_pastes = Vec::new();

        if let Ok(mut pastes_cursor) = mongo_service.get_all_pastes_metadata().await {
            while let Some(paste) = pastes_cursor.next().await {
                let paste = paste.expect("Failed to get paste");

                if paste.expires_at > 0 && paste.expires_at < now as u64 {
                    deletable_pastes.push(paste.clone());
                }

                all_pastes.push(paste);
            }
        }

        for paste in &deletable_pastes {
            let paste_id = &paste.id;
            if let Err(err) = mongo_service.delete_paste(paste_id).await {
                error!("Failed to delete paste from database: {:?}", err);
            }
            let id = &paste.id;

            if let Err(err) = mongo_service.delete_paste(id).await {
                error!("Failed to delete paste file: {}", err);
            } else {
                warn!("Deleted paste file: {}", id);
            }
        }

        if let Ok(file_names) = aws_service.list_files().await {
            for file_name in file_names {
                if all_pastes.iter().all(|paste| paste.id != file_name) {
                    if let Err(err) = aws_service.delete_file(&file_name).await {
                        error!("Failed to delete invalid file {}: {}", file_name, err);
                    } else {
                        warn!("Deleted invalid file: {}", file_name);
                    }
                }
            }
        }

        Ok(())
    }
}
