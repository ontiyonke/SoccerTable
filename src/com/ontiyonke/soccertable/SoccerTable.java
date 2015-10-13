package com.ontiyonke.soccertable;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Team{

    private int points = 0;
    private String name = null;

    Team(String name){
        super();
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getPoints(){
        return this.points;
    }

    public void incrementPoints(int points){
        this.points += points;
    }
}


class FixtureResults{
    static final int DRAW_POINTS = 1;
    static final int WIN_POINTS = 3;

    private List<Team> teams = new ArrayList<Team>();
    private List<Integer> scores = new ArrayList<Integer>();

    public List<Team> getTeams(){
        return this.teams;
    }

    public List<Integer> getScores(){
        return this.scores;
    }

    public void recordWin(Team team){
        team.incrementPoints(WIN_POINTS);
    }

    public void recordTie(Team team){
        team.incrementPoints(DRAW_POINTS);
    }

    public Team getWinner(int score1, int score2){
        if (score1 > score2){
            return this.getTeams().get(0);
        }
        else{
            return this.getTeams().get(1);
        }
    }

    public void recordResult(){
        int score1 = this.getScores().get(0);
        int score2 = this.getScores().get(1);
        if(score1 == score2){
            this.recordTie(this.getTeams().get(0));
            this.recordTie(this.getTeams().get(1));
        }
        else {
            this.recordWin(this.getWinner(score1, score2));
        }
    }

    public void processFixtures(String line){
        for (String retval: line.trim().split(",")){

            for (String teamName: retval.trim().split("[\\S+.*?]\\s+\\d+\\s*$")){
                Team team = new Team(teamName);
                this.teams.add(team);
            }

            Matcher matcher = Pattern.compile( "[\\d+]\\s*$" ).matcher( retval );
            while ( matcher.find() ) {
                this.scores.add(Integer.parseInt(matcher.group(0)));
            }

        }

    }
}


public class SoccerTable{
    private String fileName = null;
    private File inFile = null;

    private Map<String, Integer> rows = new HashMap<String, Integer>();
    private List<Team> teams = new ArrayList<Team>();

    public static void main(String[] args) {
        SoccerTable table = new SoccerTable();
        table.setFileName(args);
        table.displayTableRows();
    }

    public void setFileName(String[] args){
        if (args.length > 0) {
            this.fileName = args[0];
        } else {
            System.err.println("File containing match results is required.");
            System.exit(0);
        }
    }

    public String getFileName(){
        return this.fileName;
    }

    public void setInFile(String fileName){
        this.inFile = new File(fileName);
    }

    public File getInFile(){
        return this.inFile;
    }

    public void addTeam(Team team){
        // add team to soccer table teams if it does not exit.
        if(!this.getTeams().contains(team)){
            this.getTeams().add(team);
        }
    }

    public List<Team> getTeams(){
        return this.teams;
    }

    public Map<String, Integer> getRows(){
        return this.rows;
    }

    public void populateTableRows(){
         // Loop through the results and sum up the points for each team
        for(Team team: this.getTeams()){
            this.getRows().compute(team.getName(), (k, v) -> v == null ? team.getPoints(): v + team.getPoints());
        }
    }

    public void displayTableRows(){
        this.setInFile(this.getFileName());
        System.out.println(this.getFileName());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.getInFile()) , "UTF8"));
            String str;
            while (( str = in.readLine() ) != null ) {
                FixtureResults fixture = new FixtureResults();
                fixture.processFixtures(str);
                fixture.recordResult();
                for (Team team: fixture.getTeams()){
                    this.addTeam(team);
                }
            }

            // call method to construct the table row, a row consists of a team and points
            this.populateTableRows();

            this.displaySortedTableRows(this.sortTableRows(this.getRows()));

            in.close() ;
        }
        catch ( UnsupportedEncodingException e )
        {
            System.out.println(e.getMessage ( ) ) ;
        }
        catch (IOException e){
            System.out.println(e.getMessage ( ) ) ;
        }
        catch ( Exception e )
        {
            System.out.println(e.getMessage());
        }
    }

    public void displaySortedTableRows(Map<String, Integer> sortedMap){
        int rank = 1;
        String points_string;
        Set set = sortedMap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            points_string = ((Integer)entry.getValue() <= 1) ? "pt" : "pts";
            System.out.printf("%d. %s, %d %s\n", rank, entry.getKey(), entry.getValue(), points_string);
            rank += 1;
        }
    }

    /**
     * This method accepts an unsorted map and applies a value comparator then return a tree map ordered in descending
     * order.
     */
    public static TreeMap<String, Integer> sortTableRows (Map<String, Integer> map) {
        ValueComparator vc =  new ValueComparator(map);
        TreeMap<String,Integer> sortedMap = new TreeMap(vc);
        sortedMap.putAll(map);
        return sortedMap;
    }
}


class ValueComparator implements Comparator<String> {

    Map<String, Integer> map;

    public ValueComparator(Map<String, Integer> base) {
        this.map = base;
    }

    public int compare(String a, String b) {
        if (map.get(a) > map.get(b)) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
