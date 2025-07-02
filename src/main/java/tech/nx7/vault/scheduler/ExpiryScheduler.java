package tech.nx7.vault.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.nx7.vault.service.SnippetService;

@Component
public class ExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(ExpiryScheduler.class);
    private final SnippetService snippetService;

    public ExpiryScheduler(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void purgeExpiredSnippetsTask() {
        log.info("Running scheduled task to purge expired snippets...");
        snippetService.purgeExpiredSnippets();
        log.info("Expired snippet purge complete.");
    }
}