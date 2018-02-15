import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BrunchFriendsMain {

	// Utility class instances
	public static Scanner keyboard = new Scanner(System.in);
	public static Random r = new Random();

	final private static String filename = "/Users/kward/Desktop/Fems_Brunch_Match_Service-8-8-8.53.xlsx";
	// final private static String filename =
	// "/Users/kward/Desktop/brunch_test_responses.xlsx"

	final static boolean DEBUG = false;
	final static boolean TEST_NO_IDENTIFYING_DATA = true;
	final static boolean ACTUALLY_SEND_EMAILS = false;
	
	final static String kyleEmail = "wardkylea@gmail.com";
	private static String kyleEmailPassword = null;

	public static void main(String[] args) {
		
		System.out.println("Do you want to try a PriorityQueue? (y/n):   ");
		if (keyboard.next().toLowerCase().equals("y")) {
			Comparator<String> comparator = new KTestComparator();
			PriorityQueue<String> queue = new PriorityQueue<String>(10, comparator);
			queue.add("short");
			queue.add("very long indeed");
			queue.add("medium");
			while (queue.size() != 0) {
				System.out.println(queue.remove());
			}

			System.exit(0);
		}
		
		Map<String, Integer> namesAndLikesAchieved = new HashMap<String, Integer>();
		namesAndLikesAchieved.put("Ashley", 0);
		namesAndLikesAchieved.put("Chelsea", 0);
		namesAndLikesAchieved.put("Mo", 0);
		namesAndLikesAchieved.put("Fae", 0);
		namesAndLikesAchieved.put("Ellinor", 0);
		namesAndLikesAchieved.put("Amy", 0);
		namesAndLikesAchieved.put("Maggie", 0);

		List<ProfileBox> profiles = new ArrayList<ProfileBox>();
		int countMatches = 0;

		try {
			File myFile = new File(filename);
			FileInputStream fis = new FileInputStream(myFile);

			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);

			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();

			boolean firstRowSkipped = false;

			// Traversing over each row of XLSX file
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				if (!firstRowSkipped) {
					firstRowSkipped = true;
					continue;
				}
				int col = -1;

				ProfileBox profile = new ProfileBox();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					col++;
					Cell cell = cellIterator.next();
					switch (col) {
					case 0: {
						break;
					}

					case 1: {
						String email = cell.getStringCellValue();
						System.out.println("ITERATOR: email = " + email);
						profile.setEmail(email);
						break;
					}

					case 2: {
						String likesJargonString = cell.getStringCellValue();
						if (DEBUG) {
							System.out.println("ITERATOR: likes = " + likesJargonString);
						}
						String[] likesJargonArray = likesJargonString.split(", ");
						int likesCount = likesJargonArray.length;
						if (DEBUG) {
							System.out.println("For ID " + profile.id + ", there are " + likesCount + " likes!");
						}
						String[] likesArray = new String[likesJargonArray.length];
						for (int i = 0; i < likesCount; i++) {
							if (DEBUG) {
								System.out.println("     --> " + i + ". " + likesJargonArray[i].split(" ")[0]);
							}
							likesArray[i] = likesJargonArray[i].split(" ")[0];
							profile.addLike(likesArray[i]);
						}

						break;
					}

					case 3: {
						String name = cell.getStringCellValue();
						if (name.equals("Maggie! <3")) {
							name = "Maggie";
						}
						System.out.println("ITERATOR: name = " + name);
						profile.setName(name);
						break;
					}
					}

					// switch (cell.getCellType()) {
					// case Cell.CELL_TYPE_STRING:
					// System.out.print(cell.getStringCellValue() + "\t");
					// break;
					// case Cell.CELL_TYPE_NUMERIC:
					// System.out.print(cell.getNumericCellValue() + "\t");
					// break;
					// case Cell.CELL_TYPE_BOOLEAN:
					// System.out.print(cell.getBooleanCellValue() + "\t");
					// break;
					// }

				}

				System.out.print("PROFILE DONE: " + profile.name);
				// profile.printProfile();
				profiles.add(profile);

				System.out.println("\n");
			}

			myWorkBook.close();
		}

		catch (Exception e) {
			System.out.println("ERROR! Danger Will Robinson! Danger!");
			System.out.println("Stack trace is: ");
			e.printStackTrace();
		}

		ProfileBox[] profilesArray = profiles.toArray(new ProfileBox[profiles.size()]);

		for (int pA = 0; pA < profilesArray.length; pA++) {
			ProfileBox profileA = profilesArray[pA];
			if (DEBUG) {
				System.out.println("Loading ProfileA (" + profileA.name + "):");
			}
			if (!namesAndLikesAchieved.keySet().contains(profileA.name)) {
				System.out.println("ERROR! PROFILE.NAME: " + profileA.name + " IS NOT A REAL PERSON!");
			}
			for (String likeA : profileA.likes) {
				if (!namesAndLikesAchieved.keySet().contains(likeA)) {
					System.out.println("ERROR! LIKES: " + likeA + " IS NOT A REAL PERSON!");
				} else {
					int newLikeScore = namesAndLikesAchieved.get(likeA) + 1;
					namesAndLikesAchieved.put(likeA, newLikeScore);
				}
				boolean foundProfile = false;
				if (DEBUG) {
					System.out.println("  For ProfileA (" + profileA.name + ") looking for like [" + likeA + "]...");
				}
				for (int pB = 0; pB < profilesArray.length; pB++) {
					if (pA == pB) {
						continue;
					}

					ProfileBox profileB = profilesArray[pB];
					if (DEBUG) {
						System.out.println("    Checking against ProfileB (" + profileB.name + "):");
					}

					if (profileB.name.equals(likeA)) {
						foundProfile = true;
						if (DEBUG) {
							System.out.println("    ProfileB (" + profileB.name + ") is a like match!");
						}
						if (profileB.checkLike(profileA.name)) {
							if (!TEST_NO_IDENTIFYING_DATA) {
								System.out.println("       MATCH! ProfileA (" + profileA.name + ") and ProfileB ("
										+ profileB.name + ") like each other!");
							}
							countMatches++;

							profileA.addMutualLike(profileB.name);
							profileB.addMutualLike(profileA.name);
						} else {
							profileB.recordUnrequited();
						}
					}
				}

				if (!foundProfile) {
					System.out.println("ERROR! Never found a profile for like " + likeA + "!");
				}
			}
		}

		// Metadata
		System.out.println("Metadata:\n  There were " + (countMatches / 2) + " (of max "
				+ (profiles.size() * (profiles.size() - 1) / 2) + ") matches among " + profiles.size() + " profiles.");
		List<String> metadataScramble = new ArrayList<String>();

		for (ProfileBox profileBox : profiles) {
			String subjectLine = "BruchTinder Results!";
			String body = profileBox.name + ",\n"
					+ "This is an automated email.\nNo one has seen your data (though with only "
					+ namesAndLikesAchieved.size() + " people involved, I guess that you could figure out SOME things)."
					+ " Remember that probably not everyone is bothering with my silly app." +

			"\n\nYou liked " + profileBox.likes.size() + " people: "
					+ ProfileBox.getTextOfStringList(profileBox.likes, null, true) + "...\n";

			// Mutual Likes info
			if (profileBox.mutualLikes.size() == 0) {
				body += "\nSorry-- of the people who have bothered responding to this survey, none of them picked you back.";
				body += "\nBut there will be more brunches, and the world is full of love. You're all wonderful people.";
			} else {
				for (String mutualLikeName : profileBox.mutualLikes) {
					String mutualLikeNameAndEmail = getNameAndEmailForName(mutualLikeName, profiles);
					body += "\n   - Oh hey! " + mutualLikeNameAndEmail + " was into you too! You maybe oughta say hi.";
				}
			}

			// Unrequited info
			if (profileBox.unrequitedCount > 0) {
				body += "\n\n\n" + (profileBox.mutualLikes.size() == 0 ? "However!" : "Also!") + " You received "
						+ profileBox.unrequitedCount + (profileBox.unrequitedCount == 1 ? " instance" : " instances")
						+ " of unrequited affection. Maaaaybe go back and do the survey again, and add a few more likes?"
						+ "\n(You can only add likes; there's no deletion by omission the second time through.)";
			}

			int dropIndex = metadataScramble.size() > 0 ? r.nextInt(metadataScramble.size()) : 0;
			metadataScramble.add(dropIndex,
					"Some profileBox contained " + profileBox.likes.size() + " likes, " + profileBox.mutualLikes.size()
							+ " mutual likes, " + profileBox.unrequitedCount + " unrequiteds"
							+ (profileBox.likedSelf ? ", and some self-love" : ""));

			sendEmail(ACTUALLY_SEND_EMAILS ? profileBox.email : null, subjectLine, body);
		}

		// Metadata
		System.out.println("\n\n\nEntrants got likes:");
		for (Map.Entry<String, Integer> entry : namesAndLikesAchieved.entrySet()) {
			System.out.println("  " + entry.getKey() + " " + entry.getValue());
		}

		System.out.println("\n\nMetadata summaries:");
		for (String metaEntry : metadataScramble) {
			System.out.println("  " + metaEntry);
		}
	}

	private static String getNameAndEmailForName(String name, List<ProfileBox> profiles) {
		for (ProfileBox profileBox : profiles) {
			if (profileBox.name.equals(name)) {
				return name + " (" + profileBox.email + ")";
			}
		}

		System.out.println("ERROR getNameAndEmailForName()! Could not find profileBox for " + name + "!");

		return null;
	}

	private static void sendEmail(String emailTo, String subjectLine, String body) {
		if (!TEST_NO_IDENTIFYING_DATA) {
			System.out.println("\n\n\n\nEMAIL: " + subjectLine + "\n\n" + body);
		}
		if (emailTo == null) {
			emailTo = "wardkylea@gmail.com";
			// KTEST
			return;
		}

		// Sender's email ID needs to be mentioned
		final String pass = getEmailFromUser();

		// Get system properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		// Get the default Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(kyleEmail, pass);
			}
		});

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(kyleEmail));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

			// Set Subject: header field
			message.setSubject(subjectLine);

			// Now set the actual message
			message.setText(body);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	private static String getEmailFromUser() {
		if (kyleEmailPassword == null) {
			System.out.print("What's your gmail password, Kyle?   ");
			kyleEmailPassword = keyboard.next();
		}

		return kyleEmailPassword;
	}

}

class KTestComparator implements Comparator<String>
{
    @Override
    public int compare(String x, String y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.length() < y.length())
        {
            return 1;
        }
        if (x.length() > y.length())
        {
            return -1;
        }
        return 0;
    }
}
