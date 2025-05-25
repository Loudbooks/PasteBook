use sea_orm_migration::{prelude::*, schema::*};

#[derive(DeriveMigrationName)]
pub struct Migration;

#[async_trait::async_trait]
impl MigrationTrait for Migration {
    async fn up(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .create_index(
                Index::create()
                    .name("idx-user-id")
                    .table(User::Table)
                    .col(User::Id).to_owned(),
            ).await?;

        manager
            .create_index(
                Index::create()
                    .name("idx-paste-creator-ip")
                    .table(Paste::Table)
                    .col(Paste::CreatorIp).to_owned(),
            )
            .await?;
        
        manager
            .create_index(
                Index::create()
                    .name("idx-paste-id")
                    .table(Paste::Table)
                    .col(Paste::Id).to_owned(),
            )
            .await?;
        
        manager
            .create_index(
                Index::create()
                    .name("idx-paste-expires-at")
                    .table(Paste::Table)
                    .col(Paste::ExpiresAt).to_owned(),
            )
            .await?;
        
        Ok(())
    }

    async fn down(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .drop_index(Index::drop().name("idx-user-id").table(User::Table).to_owned())
            .await?;

        manager
            .drop_index(Index::drop().name("idx-paste-creator-ip").table(Paste::Table).to_owned())
            .await?;
        manager
            .drop_index(Index::drop().name("idx-paste-expires-at").table(Paste::Table).to_owned())
            .await?;
        Ok(())
    }
}


#[derive(Iden)]
#[iden(rename = "user_metadata")]
#[allow(dead_code)]
enum User {
    Table,
    Ip,
    Id,
    Requests,
    CreatedAt,
    Banned,
}

#[derive(Iden)]
#[iden(rename = "paste_metadata")]
#[allow(dead_code)]
enum Paste {
    Table,
    Id,
    Title,
    Created,
    ReportBook,
    Wrap,
    CreatorIp,
    ExpiresAt,
}

#[derive(Iden)]
#[iden(rename = "paste_content")]
#[allow(dead_code)]
enum PasteContent {
    Table,
    Id,
    Content,
}