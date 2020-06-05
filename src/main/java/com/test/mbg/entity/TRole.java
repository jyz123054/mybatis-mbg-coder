package com.test.mbg.entity;

import java.util.List;

public class TRole extends SerializableExtend {
    private Integer id;
    private String roleName;
    private String note;
    
    private List<TUser> users;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

	public List<TUser> getUsers() {
		return users;
	}

	public void setUsers(List<TUser> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "TRole [id=" + id + ", roleName=" + roleName + ", note=" + note + ", users=" + users + "]";
	}
	
}