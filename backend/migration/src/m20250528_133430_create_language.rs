use sea_orm_migration::{prelude::*};

#[derive(DeriveMigrationName)]
pub struct Migration;

#[async_trait::async_trait]
impl MigrationTrait for Migration {
    async fn up(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .alter_table(
                Table::alter()
                    .table(Paste::Table)
                    .add_column(
                        ColumnDef::new(Paste::Language)
                            .string()
                            .default(None::<String>),
                    )
                    .to_owned(),
            ).await
    }

    async fn down(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .alter_table(
                Table::alter()
                    .table(Paste::Table)
                    .drop_column(Paste::Language)
                    .to_owned(),
            )
            .await
    }
}

#[derive(Iden)]
#[iden(rename = "paste_metadata")]
enum Paste {
    Table,
    Language
}