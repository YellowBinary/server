package org.yellowbinary.cms.server.core.model.content.navigation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "pageIdNavigation")
@Table(name = "navigation_page_id")
public class InternalPageIdNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NotNull
    @Column(unique = true)
    public String identifier;

    @NotNull
    public String pageId;

    public InternalPageIdNavigation() {
    }

}
