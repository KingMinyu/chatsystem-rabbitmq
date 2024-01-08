package com.example.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class ChatRoom {

    private final String id;
    private final Set<String> customers = new HashSet<>();
    private String assignedCustomer;

    public boolean isAvailable() {
        return assignedCustomer == null;
    }

	public void setAssignedCustomer(String user) {
		// TODO Auto-generated method stub
		
	}
}