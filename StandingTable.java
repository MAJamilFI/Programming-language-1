import java.util.*;

public class StandingTable {

    // Constants to represent indices in the pointTable array
    private static final int TEAM_NAME_INDEX = 0;
    private static final int MATCHES_PLAYED_INDEX = 1;
    private static final int WINS_INDEX = 2;
    private static final int DRAWS_INDEX = 3;
    private static final int LOSSES_INDEX = 4;
    private static final int GOALS_FOR_INDEX = 5;
    private static final int GOALS_AGAINST_INDEX = 6;
    private static final int POINTS_INDEX = 7;

    // 2-dimensional array to store team data
    private static String[][] pointTable = new String[25][8];
    // Each team's data: [0]:Team name, [1]:match-played, [2]:Wins, [3]:Draws,
    // [4]:Losses, [5]:goalsFor, [6]: goalsAgainst, [7]:Points

    // Variable to keep track of the number of teams on the table
    private static int teamCount = 0;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        // Implementing a while loop to reprompt the user for functionality choice
        while (true) {
            System.out.print("\nProgram functionalities: \n\t1 = Add scores of a game,\n\t2 = Print standings Table,\n\t3 = To exit the program\n\nChoose desired function: ");

            int choice;

            // Try-catch block to handle invalid input
            try {
                choice = scan.nextInt();
                if (choice < 1 || choice > 3) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please only enter a number between 1 and 3.");
                scan.nextLine(); // Consume invalid input
                continue;
            }

            System.out.println();

            if (choice == 1) {
                // Call the function to add data to the point table
                addScores(scan);
            } else if (choice == 2) {
                // Call the function to print standings
                printStandings();
            } else if (choice == 3) {
                // Exiting the program if the user chooses to
                break;
            }
        }
    }

    // Function to add scores of a game
    private static void addScores(Scanner scan) {
        try {
            System.out.print("Enter Home team name (Be careful about the spelling): ");
            String homeTeam = scan.next();

            System.out.print("Enter away team name (Be careful about the spelling): ");
            String awayTeam = scan.next();

            System.out.print("Goals for home team: ");
            int homeTeamGoals = scan.nextInt();

            // Taking input for away team goals
            System.out.print("Goals for away team: ");
            int awayTeamGoals = scan.nextInt();

            addResult(homeTeam, homeTeamGoals, awayTeamGoals);
            addResult(awayTeam, awayTeamGoals, homeTeamGoals);

            System.out.println("\nThank you. Score added Successfully!\n");

        } catch (InputMismatchException e) {

            System.out.println("Invalid input. Please enter valid values for goals.");
            scan.nextLine(); // Consume invalid input

        } catch (NumberFormatException e) {

            System.out.println("Error processing match result. Please enter valid numerical values.");
            
        }
    }

    // Function to update the point table based on match results
    private static void addResult(String teamName, int goalsFor, int goalsAgainst) {
        int teamIndex = findTeamIndex(teamName);
        if (teamIndex == -1) {
            // If the team is not in the table, add a new entry
            pointTable[teamCount][TEAM_NAME_INDEX] = teamName;
            pointTable[teamCount][MATCHES_PLAYED_INDEX] = "0";
            pointTable[teamCount][WINS_INDEX] = "0";
            pointTable[teamCount][DRAWS_INDEX] = "0";
            pointTable[teamCount][LOSSES_INDEX] = "0";
            pointTable[teamCount][GOALS_FOR_INDEX] = String.valueOf(goalsFor);
            pointTable[teamCount][GOALS_AGAINST_INDEX] = String.valueOf(goalsAgainst);
            pointTable[teamCount][POINTS_INDEX] = "0";
            teamCount++;
        } else {
            // If the team is already in the table, update its statistics
            int goalsForSoFar = Integer.parseInt(pointTable[teamIndex][GOALS_FOR_INDEX]);
            int goalsAgainstSoFar = Integer.parseInt(pointTable[teamIndex][GOALS_AGAINST_INDEX]);
            goalsFor += goalsForSoFar;
            goalsAgainst += goalsAgainstSoFar;
            pointTable[teamIndex][GOALS_FOR_INDEX] = String.valueOf(goalsFor);
            pointTable[teamIndex][GOALS_AGAINST_INDEX] = String.valueOf(goalsAgainst);
        }

        // Update win/loss/draw statistics
        if (goalsFor > goalsAgainst) {
            updateStats(teamName, WINS_INDEX, 3);
        } else if (goalsFor < goalsAgainst) {
            updateStats(teamName, LOSSES_INDEX, 0);
        } else {
            updateStats(teamName, DRAWS_INDEX, 1);
        }
    }

    // Function to update team statistics
    private static void updateStats(String teamName, int statIndex, int pointsToAdd) {
        int teamIndex = findTeamIndex(teamName);
        int currentStat = Integer.parseInt(pointTable[teamIndex][statIndex]);
        pointTable[teamIndex][statIndex] = String.valueOf(currentStat + 1);
        pointTable[teamIndex][POINTS_INDEX] = String.valueOf(Integer.parseInt(pointTable[teamIndex][POINTS_INDEX]) + pointsToAdd);

        pointTable[teamIndex][MATCHES_PLAYED_INDEX] = String.valueOf(Integer.parseInt(pointTable[teamIndex][MATCHES_PLAYED_INDEX]) + 1);
    }

    // Function to find the index of a team in the point table
    private static int findTeamIndex(String teamName) {
        for (int i = 0; i < teamCount; i++) {
            if (pointTable[i][TEAM_NAME_INDEX] != null && pointTable[i][TEAM_NAME_INDEX].equalsIgnoreCase(teamName)) {
                return i;
            }
        }
        return -1; // Return -1 if the team is not found
    }

    // Function to print the standings table
    private static void printStandings() {
        // Comparators for sorting the table
        Comparator<String[]> pointsComparator = Comparator.comparingInt((String[] team) -> Integer.parseInt(team[POINTS_INDEX]))
                .reversed();

        Comparator<String[]> goalDifferenceComparator = (team1, team2) -> {
            int pointsDiff = Integer.parseInt(team2[POINTS_INDEX]) - Integer.parseInt(team1[POINTS_INDEX]);
            if (pointsDiff != 0) {
                return pointsDiff;
            } else {
                int goalDiff1 = Integer.parseInt(team1[GOALS_FOR_INDEX]) - Integer.parseInt(team1[GOALS_AGAINST_INDEX]);
                int goalDiff2 = Integer.parseInt(team2[GOALS_FOR_INDEX]) - Integer.parseInt(team2[GOALS_AGAINST_INDEX]);
                return Integer.compare(goalDiff2, goalDiff1);
            }
        };

        // Sorting the table based on points and goal difference
        Arrays.sort(pointTable, 0, teamCount, pointsComparator.thenComparing(goalDifferenceComparator));

        // Printing the table
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n", "Team", "Match played", "Win", "Draw", "Lost", "Points", "Goals(GF-GA)");
        for (int i = 0; i < teamCount; i++) {
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n", pointTable[i][TEAM_NAME_INDEX],
                    pointTable[i][MATCHES_PLAYED_INDEX], pointTable[i][WINS_INDEX], pointTable[i][DRAWS_INDEX],
                    pointTable[i][LOSSES_INDEX], pointTable[i][POINTS_INDEX],
                    pointTable[i][GOALS_FOR_INDEX] + "-" + pointTable[i][GOALS_AGAINST_INDEX]);
        }
    }
}
