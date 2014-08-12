package org.yellowbinary.cms.server.core.model.content.navigation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "groupHolderNavigation")
@Table(name = "navigation_group_holder")
public class GroupHolderNavigation {

    public GroupHolderNavigation() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NotNull
    @Column(unique = true)
    public String identifier;

    @NotNull
    public String title;

}
