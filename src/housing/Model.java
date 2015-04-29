package housing;

import java.util.ArrayList;

import sim.engine.SimState;
import sim.engine.Steppable;
import ec.util.MersenneTwisterFast;

/**
 * Simulates the housing market.
 * 
 * @author daniel
 *
 */
@SuppressWarnings("serial")
public class Model extends SimState implements Steppable {

	public Model(long seed) {
		super(seed);
	}

	// START: INITIAL POPULATION OF PERSON AGENTS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/** This method creates the setting for the simulation. Most importantly, the initial population of person and 
	 * household agents is created.
	 */
	public void start() {
		super.start();
        schedule.scheduleRepeating(this);

        makeInitialPopulation();

		t=0;
		System.out.println();
		System.out.println("Number of Households: " + households.size());
		System.out.println("Number of Households: " + Household.HouseholdCount);
		System.out.println("Number of People: " + persons.size());
		System.out.println("Number of people: " + Person.PersonCount);
		System.out.println("Number of marriages: " + Model.MarriageCount);
		System.out.println();
	}

	
	// STEP 
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/** This method executes the simulation steps. After the step() method of each PA has been executed, 
	 * the person and personAll lists are updated and the persons_justBorn and persons_justDied lists are cleared.
	 */
	public void step(SimState simulationStateNow) {
		int j;
        if (schedule.getTime() >= N_STEPS) simulationStateNow.kill();
        
		System.out.println("Step " + t + " begins ...");	
		
		// step for each person agent //////
		for (Person p : persons) {
			p.step();
			if(Person.PersonCount == 0) {
				System.out.println("Everybody dead!");
				simulationStateNow.kill();
			}
		}
/*		if(Household.HouseholdCount != Model.households.size()) {
			simulationStateNow.kill();
			System.out.println("Fuck ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}*/

		System.out.println("Births: " + persons_justborn.size());
		System.out.println("Deaths: " + persons_justdied.size());
		System.out.println("People: " + persons.size());
		//System.out.println("Orphans: " + Model.OrphanCount);
		
		// update person lists
		personsAll.addAll(persons_justborn);
		persons.removeAll(persons_justdied);
		persons.addAll(persons_justborn);
		persons_justdied.clear();
		persons_justborn.clear();
		
		// step for each household agent //////
/*		for (Household h : households) {
			h.step();
		}		
*/
		t++;
	}
	
	
	// FINISH
	/** This method finishes the simulation */
	public void finish() {
		
		System.out.println();
		System.out.println("Number of households, Total: " + Household.HouseholdCount);
		System.out.println("Number of households, Total: " + households.size());
		System.out.println("Number of people: " + Person.PersonCount);
		System.out.println("Number of people: " + persons.size());
		System.out.println("Number of people ever: " + Person.PIDCount);		
		System.out.println("Number of households ever: " + Household.HIDCount);		
		System.out.println("Number of marriages: " + Model.MarriageCount);
		System.out.println("Number of marriages with at least one partner still a child: " + Model.DependentChildMarriageCount);
		System.out.println("Number of orphans: " + Model.orphans.size());
		System.out.println();
		super.finish();
		
	}
	
	/** This method creates the initial population of person and household agents. First, the person agents are created.
	 * Second, marriages are established.
	 */
	public void makeInitialPopulation() {
		
		females_by_agegroup.clear();
		females_by_agegroup.add(female_singles_16to19);
		females_by_agegroup.add(female_singles_20to24);
		females_by_agegroup.add(female_singles_25to29);
		females_by_agegroup.add(female_singles_30to34);
		females_by_agegroup.add(female_singles_35to39);
		females_by_agegroup.add(female_singles_40to44);
		females_by_agegroup.add(female_singles_45to49);
		females_by_agegroup.add(female_singles_50to54);
		females_by_agegroup.add(female_singles_55to59);
		females_by_agegroup.add(female_singles_60to64);
		females_by_agegroup.add(female_singles_65to69);
		females_by_agegroup.add(female_singles_70to74);
		females_by_agegroup.add(female_singles_75to79);
		females_by_agegroup.add(female_singles_80to84);
		females_by_agegroup.add(female_singles_85plus);

		males_by_agegroup.clear();
		males_by_agegroup.add(male_singles_16to19);
		males_by_agegroup.add(male_singles_20to24);
		males_by_agegroup.add(male_singles_25to29);
		males_by_agegroup.add(male_singles_30to34);
		males_by_agegroup.add(male_singles_35to39);
		males_by_agegroup.add(male_singles_40to44);
		males_by_agegroup.add(male_singles_45to49);
		males_by_agegroup.add(male_singles_50to54);
		males_by_agegroup.add(male_singles_55to59);
		males_by_agegroup.add(male_singles_60to64);
		males_by_agegroup.add(male_singles_65to69);
		males_by_agegroup.add(male_singles_70to74);
		males_by_agegroup.add(male_singles_75to79);
		males_by_agegroup.add(male_singles_80to84);
		males_by_agegroup.add(male_singles_85plus);

        // create persons
		for(int p = 0; p<N_PERSON; ++p) { 
			persons.add(new Person());
		}        
		personsAll.addAll(persons);
		
		for (Person p : persons) {
			p.setUpInitialMarriages();
		}
	}
	

	////////////////////////////////////////////////////////////////////////
	// Getters/setters for the console
	////////////////////////////////////////////////////////////////////////
	
/*
	public Household.Config getLifecycleConfig() {
		return(new Household.Config());
	}
*/	

	public static int getN_STEPS() {
		return N_STEPS;
	}

	public static void setN_STEPS(int n_STEPS) {
		N_STEPS = n_STEPS;
	}

	public String nameN_STEPS() {return("Number of timesteps");}

	////////////////////////////////////////////////////////////////////////

	public static final int N_HOUSEHOLD = 1; // number of households	
	public static final int N_PERSON = 10000; // number of households	
	public static final int Nh = 4100; // number of houses
	public static int N_STEPS = Person.LifecycleFreq*200; // timesteps

	public static Firm								firm;
	public static ArrayList<Person> 				personsAll = new ArrayList<Person>(); // record of all people who ever lived
	public static ArrayList<Person> 				persons = new ArrayList<Person>();
	public static ArrayList<Person> 				persons_justborn = new ArrayList<Person>();
	public static ArrayList<Person> 				persons_justdied = new ArrayList<Person>();
	public static ArrayList<Person> 				orphans = new ArrayList<Person>();
	
	
	public static ArrayList<ArrayList<Person>> 		females_by_agegroup = new ArrayList<ArrayList<Person>>();
	public static ArrayList<Person> 				female_singles_16to19 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_20to24 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_25to29 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_30to34 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_35to39 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_40to44 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_45to49 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_50to54 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_55to59 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_60to64 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_65to69 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_70to74 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_75to79 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_80to84 = new ArrayList<Person>();
	public static ArrayList<Person> 				female_singles_85plus = new ArrayList<Person>();

	public static ArrayList<ArrayList<Person>> 		males_by_agegroup = new ArrayList<ArrayList<Person>>();
	public static ArrayList<Person> 				male_singles_16to19 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_20to24 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_25to29 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_30to34 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_35to39 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_40to44 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_45to49 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_50to54 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_55to59 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_60to64 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_65to69 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_70to74 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_75to79 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_80to84 = new ArrayList<Person>();
	public static ArrayList<Person> 				male_singles_85plus = new ArrayList<Person>();

	
	public static ArrayList<Household> 				households = new ArrayList<Household>();
	public static ArrayList<Household> 				householdsAll = new ArrayList<Household>();
	public static int 								t;
	public static MersenneTwisterFast				rand = new MersenneTwisterFast(1L);
	
	public static int 								MarriageCount = 0; 
	public static int								DependentChildMarriageCount = 0;
	public static int 								OrphanCount = 0; 

	
	
}