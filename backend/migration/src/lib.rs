pub use sea_orm_migration::prelude::*;

mod m20250425_104512_create_user_and_paste_tables;
mod m20250504_040542_remove_reportbook;

pub struct Migrator;

#[async_trait::async_trait]
impl MigratorTrait for Migrator {
    fn migrations() -> Vec<Box<dyn MigrationTrait>> {
        vec![
            Box::new(m20250425_104512_create_user_and_paste_tables::Migration),
            Box::new(m20250504_040542_remove_reportbook::Migration),
        ]
    }
}
