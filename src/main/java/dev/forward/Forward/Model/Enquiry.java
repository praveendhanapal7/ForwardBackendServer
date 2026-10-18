package dev.forward.Forward.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String requirement;

    private String phone;

    private String caption;

    private boolean checkBox;

    @Column(name = "client_name")
    private String clientName;

    private String status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        if (status == null || status.isBlank()) {
            status = "NEW";
        }
    }
}
