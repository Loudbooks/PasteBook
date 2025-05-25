use sea_orm_migration::{prelude::*, schema::*};

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
                        ColumnDef::new(Paste::Burn)
                            .boolean()
                            .not_null()
                            .default(false),
                    )
                    .to_owned(),
            ).await
    }

    async fn down(&self, manager: &SchemaManager) -> Result<(), DbErr> {
        manager
            .alter_table(
                Table::alter()
                    .table(Paste::Table)
                    .drop_column(Paste::Burn)
                    .to_owned(),
            )
            .await
    }
}

#[derive(Iden)]
#[iden(rename = "paste_metadata")]
enum Paste {
    Table,
    Burn
}