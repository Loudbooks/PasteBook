use std::sync::Arc;
use std::time::Duration;
use chrono::Utc;
use tokio::spawn;
use tokio::time::interval;
use log::{error, trace, debug};
use crate::database::postgres_service::PostgresService;

pub struct DeleteHandler {
    postgres_service: Arc<PostgresService>,
}

impl DeleteHandler {
    pub fn new(postgres_service: Arc<PostgresService>) -> Self {
        let handler = Self {
            postgres_service,
        };

        handler.start_delete_loop();
        handler
    }

    pub(crate) fn start_delete_loop(&self) {
        let postgres_service = Arc::clone(&self.postgres_service);

        spawn(async move {
            let mut interval = interval(Duration::from_secs(600));
            loop {
                interval.tick().await;
                if let Err(err) = Self::delete_files(&postgres_service).await {
                    error!("Error during deletion process: {}", err);
                }
            }
        });
    }

    async fn delete_files(
        postgres_service: &Arc<PostgresService>,
    ) -> Result<(), String> {
        trace!("Deleting files...");
        let now = Utc::now().timestamp_millis();
        let mut deletable_pastes = Vec::new();
        let mut all_pastes = Vec::new();

        match postgres_service.get_all_pastes_metadata().await {
            Ok(pastes) => {
                for paste in pastes {
                    if paste.expires_at > 0 && paste.expires_at < now {
                        trace!("Deleting expired paste: {}", paste.id);
                        deletable_pastes.push(paste.clone());
                    }

                    all_pastes.push(paste);
                }
            }
            Err(err) => {
                return Err(format!("Failed to retrieve pastes from database: {}", err));
            }
        }

        for paste in &deletable_pastes {
            let id = &paste.id;

            match postgres_service.delete_paste(id).await { Err(err) => {
                error!("Failed to delete paste file: {}", err);
            } _ => {
                debug!("Deleted paste file: {}", id);
            }}
        }
        
        Ok(())
    }
}
