package org.sheshant.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sheshant.messenger.database.DatabaseClass;
import org.sheshant.messenger.model.Profile;

public class ProfileService {

	public ProfileService() {
		
	}
	
	public List<Profile> getAllProfiles() {
		return DatabaseClass.getAllProfiles();
	}
	
	public Profile getProfile(String profileName) {
		return DatabaseClass.getProfile(profileName);
	}
	
	public Profile addProfile(Profile profile) {
		profile.setId(DatabaseClass.getMaxProfileID() + 1);
		return DatabaseClass.addProfile(profile);
	}
	
	public Profile updateProfile(Profile profile) {
		return DatabaseClass.updateProfile(profile);
	}
	
	public Profile removeProfile(String profileName) {
		return DatabaseClass.removeProfile(profileName);
	}
	
	
}
