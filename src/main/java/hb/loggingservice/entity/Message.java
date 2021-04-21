package hb.loggingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MESSAGE")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQ")
    @SequenceGenerator(name = "MESSAGE_SEQ", sequenceName = "MESSAGE_SEQ", allocationSize = 1)
    private Long id;

    @Column
    private String message;
    @Column
    private String name;
    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private Log log;

    @Override
    public String toString() {
        return "{" +
            "id=" + id +
            ", message='" + message + '\'' +
            ", name='" + name + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
