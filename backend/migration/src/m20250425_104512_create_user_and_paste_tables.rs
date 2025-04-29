use sea_orm_migration::prelude::*;

#[derive(DeriveMigrationName)]
pub struct Migration;

#[async_trait::async_trait]
impl MigrationTrait for Migration {
    async fn up(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .create_table(
                Table::create()
                    .table(User::Table)
                    .if_not_exists()
                    .col(ColumnDef::new(User::Ip).string().not_null().primary_key())
                    .col(ColumnDef::new(User::Id).string().not_null())
                    .col(ColumnDef::new(User::Requests).big_unsigned().not_null())
                    .col(ColumnDef::new(User::CreatedAt).big_integer().not_null())
                    .col(ColumnDef::new(User::Banned).boolean().not_null())
                    .to_owned(),
            )
            .await?;

        manager
            .create_table(
                Table::create()
                    .table(Paste::Table)
                    .if_not_exists()
                    .col(ColumnDef::new(Paste::Id).string().not_null().primary_key())
                    .col(ColumnDef::new(Paste::Title).string().not_null())
                    .col(ColumnDef::new(Paste::Created).big_unsigned().not_null())
                    .col(ColumnDef::new(Paste::ReportBook).boolean().not_null())
                    .col(ColumnDef::new(Paste::Wrap).boolean().not_null())
                    .col(ColumnDef::new(Paste::CreatorIp).string().not_null())
                    .col(ColumnDef::new(Paste::ExpiresAt).big_unsigned().not_null())
                    .to_owned(),
            )
            .await?;

        manager
            .create_table(
                Table::create()
                    .table(PasteContent::Table)
                    .if_not_exists()
                    .col(ColumnDef::new(PasteContent::Id).string().not_null().primary_key())
                    .col(ColumnDef::new(PasteContent::Content).text().not_null())
                    .to_owned(),
            )
            .await
    }

    async fn down(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager.drop_table(Table::drop().table(Paste::Table).to_owned()).await?;
        manager.drop_table(Table::drop().table(User::Table).to_owned()).await?;
        manager.drop_table(Table::drop().table(PasteContent::Table).to_owned()).await
    }
}

#[derive(Iden)]
#[iden(rename = "user_metadata")]
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
enum PasteContent {
    Table,
    Id,
    Content,
}