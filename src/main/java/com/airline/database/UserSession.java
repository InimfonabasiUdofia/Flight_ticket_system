package com.airline.database;
import java.time.LocalDateTime;

class UserSession {
    private String sessionId;
    private User user;
    private LocalDateTime expiresAt;
    
    public UserSession(String sessionId, User user, LocalDateTime expiresAt) {
        this.sessionId = sessionId;
        this.user = user;
        this.expiresAt = expiresAt;
    }
    
    public String getSessionId() { return sessionId; }
    public User getUser() { return user; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}

// SessionManager.java
class SessionManager {
    private static UserSession currentSession;
    
    public static void setCurrentSession(UserSession session) {
        currentSession = session;
    }
    
    public static UserSession getCurrentSession() {
        return currentSession;
    }
    
    public static User getCurrentUser() {
        return currentSession != null ? currentSession.getUser() : null;
    }
    
    public static void logout() {
        if (currentSession != null) {
            DatabaseManager.logoutSession(currentSession.getSessionId());
            currentSession = null;
        }
    }
}
