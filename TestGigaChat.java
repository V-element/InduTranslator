package test;

import java.util.Base64;

public class TestGigaChat {
    public static void main(String[] args) throws Exception {
        String apiKey = "MDE5ZjkwMjQtZmRiMC03NTg4LTkyYzEtMmI3YjNhZjhhZDU0OmI0M2RhNzgyLWNjZmYtNDJjNS05NmFkLTliYTNlMjU5MGFlYw==";
        
        // Decode base64
        String decoded = new String(Base64.getDecoder().decode(apiKey));
        System.out.println("Decoded: " + decoded);
        
        String[] parts = decoded.split(":", 2);
        System.out.println("Client ID: " + parts[0]);
        System.out.println("Client Secret: " + (parts.length > 1 ? parts[1] : "N/A"));
        
        // Encode for Basic Auth
        String credentials = decoded;
        String encodedAuth = Base64.getEncoder().encodeToString(credentials.getBytes());
        System.out.println("Basic Auth: " + encodedAuth.substring(0, Math.min(30, encodedAuth.length())) + "...");
    }
}
