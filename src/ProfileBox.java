import java.util.ArrayList;
import java.util.List;

public class ProfileBox {

	private static int nextProfileID = 0;
	public int id;
	public String name;
	public String email;
	public int unrequitedCount = 0;
	public List<String> likes = new ArrayList<String>();
	public List<String> mutualLikes = new ArrayList<String>();
	public boolean likedSelf = false;
	
	ProfileBox() {
		setProfileID();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	private void setLikedSelf() {
		likedSelf = true;
	}
	
	public void addLike(String likeName) {
		// System.out.println("ProfileBox.ID="+id+".addLike: "+likeName);
		if(likeName.equals(name)) {
			setLikedSelf();
		}
		else {
			likes.add(likeName);
		}
	}
	
	public void addMutualLike(String likeName) {
		// System.out.println("ProfileBox.ID="+id+".addLike: "+likeName);
		if(!mutualLikes.contains(likeName)) {
			mutualLikes.add(likeName);
		}
	}
	
	public boolean checkLike(String checkName) {
		for(String like : likes) {
			if(checkName.equals(like)) {
				return true;
			}
		}
		return false;
	}
	
	public void recordUnrequited() {
		unrequitedCount++;
	}
	
	private void setProfileID() {
		id = nextProfileID;
		nextProfileID++;
	}
	
	public void printProfile() {
		System.out.println(id + ". "+name+" ("+email+") is into: " + getTextOfStringList(likes, null, true)+" ... Mutual likes: " + getTextOfStringList(mutualLikes, null, true));
	}
	
	public static String getTextOfStringList(List<String> stringList, Integer limitToThisManyItems, Boolean andNotOr) {
		String[] stringArray = stringList.toArray(new String[stringList.size()]);
		return getTextOfStringArray(stringArray, limitToThisManyItems, andNotOr);
	}
	
	/**
	 * Returns a String which lists all the items in the array in order.
	 * @param stringArray
	 * @param limitToThisManyItems
	 * @param andNotOr - if this is null, there is just a space between entries. Otherwise, there is a ", " and
	 * 		an "and" or "or" before the final entry.
	 * @return
	 */
	public static String getTextOfStringArray(String stringArray[], Integer limitToThisManyItems, Boolean andNotOr) {
		int count = stringArray.length;
		if(limitToThisManyItems != null && limitToThisManyItems < count) {
			count = limitToThisManyItems;
		}
		String conjunction = (andNotOr == null) ? "" : (andNotOr ? "and " : "or ");
		
		switch(count) {
			case 0: { return ""; }
			case 1: { return stringArray[0]; }
			case 2: { return stringArray[0] + ", " + conjunction + stringArray[1]; }
			default: {
				String text = "";
				for (int a = 0; a < count; a++) {
					text += stringArray[a];
					if(a < count-2) { text += ", "; }
					else if(a < count-1) { text += ", " + conjunction; }
				}
				return text;
			}
		}
	}
	
}
