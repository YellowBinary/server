package org.yellowbinary.server.core.model.content.navigation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "exeternalLinkNavigation")
@Table(name = "navigation_external_link")
public class ExternalLinkNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NotNull
    @Column(unique = true)
    public String identifier;

    @NotNull
    public String title;

    @NotNull
    public String link;

    public ExternalLinkNavigation() {

    }

}
