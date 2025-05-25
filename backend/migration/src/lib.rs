pub use sea_orm_migration::prelude::*;

mod m20250425_104512_create_user_and_paste_tables;
mod m20250504_040542_remove_reportbook;
mod m20250525_002244_create_index;
mod m20250525_013131_pastebook_redesign;

pub struct Migrator;

#[async_trait::async_trait]
impl MigratorTrait for Migrator {
    fn migrations() -> Vec<Box<dyn MigrationTrait>> {
        vec![
            Box::new(m20250425_104512_create_user_and_paste_tables::Migration),
            Box::new(m20250504_040542_remove_reportbook::Migration),
            Box::new(m20250525_002244_create_index::Migration),
            Box::new(m20250525_013131_pastebook_redesign::Migration),
        ]
    }
}
