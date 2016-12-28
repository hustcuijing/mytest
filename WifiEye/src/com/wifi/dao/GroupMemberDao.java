package com.wifi.dao;

import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.wifi.model.GroupMember;
import com.wifi.model.PageBean;

public interface GroupMemberDao {
    public List<GroupMember> getMember(String name,PageBean pagebean);
    public int getMemeberCount(String name);
    public int deleteMember(String names,String usr_macs);
    public boolean isExitsMember(String usr_mac,String name);
    public boolean updateMemeber(String usr_mac,String name);
    public void addMember(String usr_mac,String name);
    public List<String> getMemeberByGroup(String group);
}
