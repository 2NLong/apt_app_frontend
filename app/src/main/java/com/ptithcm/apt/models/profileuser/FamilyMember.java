package com.ptithcm.apt.models.profileuser;


public class FamilyMember {
    private String name;
    private String relation;
    private int iconResId;

    public FamilyMember(String name, String relation, int iconResId) {
        this.name = name;
        this.relation = relation;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public int getIconResId() {
        return iconResId;
    }
}
