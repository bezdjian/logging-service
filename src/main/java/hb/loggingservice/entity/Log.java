package hb.loggingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOG")
public class Log {
    @Id
    private Long id;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("createdAt desc ")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();
}
