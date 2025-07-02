package tech.nx7.vault.util;

import com.github.f4b6a3.uuid.UuidCreator;

public class IdGenerator {
    public static String generateUUIDv7() {
        return UuidCreator.getTimeOrderedEpoch().toString();
    }
}