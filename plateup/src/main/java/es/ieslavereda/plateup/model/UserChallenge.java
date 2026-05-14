package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserChallenges")
@IdClass(UserChallengeId.class)
public class UserChallenge {

    @Id
    private Long user_id;

    @Id
    private Long challenge_id;

    private double progress;
    private String status;
    private LocalDateTime started_at;
    private LocalDateTime finished_at;

    // Getters y setters
    public Long getUser_id() { return user_id; }
    public void setUser_id(Long user_id) { this.user_id = user_id; }
    public Long getChallenge_id() { return challenge_id; }
    public void setChallenge_id(Long challenge_id) { this.challenge_id = challenge_id; }
    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStarted_at() { return started_at; }
    public void setStarted_at(LocalDateTime started_at) { this.started_at = started_at; }
    public LocalDateTime getFinished_at() { return finished_at; }
    public void setFinished_at(LocalDateTime finished_at) { this.finished_at = finished_at; }
}
