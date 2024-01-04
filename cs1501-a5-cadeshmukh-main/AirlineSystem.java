import java.util.*;
import java.io.*;

final public class AirlineSystem implements AirlineInterface 
{ 
  public String [] cityNames = null;
  public Digraph G = null;
  public Set<String> hs = null;
  public static final int INFINITY = Integer.MAX_VALUE;
  
  public boolean loadRoutes(String fileName){ 
  try
     {
      Scanner scan = new Scanner(new FileInputStream(fileName));
      int v = Integer.parseInt(scan.nextLine());
      G = new Digraph(v);
      cityNames = new String[v];
      for(int i = 0; i < v; i++) 
      {
        cityNames[i] = scan.nextLine();
        hs = new HashSet<String>();
      }
      for(int i = 0; i < cityNames.length; i++) 
      {
        hs.add(cityNames[i]);
      }

      while(scan.hasNext())
      {
        int from = scan.nextInt();
        int to = scan.nextInt();
        int route = scan.nextInt();
        double rates = scan.nextDouble();
        G.addEdge(new Route(cityNames[to - 1], cityNames[from - 1], route, rates));
        G.addEdge(new Route(cityNames[from - 1], cityNames[to - 1], route, rates));
        try
        {
          scan.nextLine();
        }
        catch(NoSuchElementException ex)
        {
          
        }
      }
    scan.close();

    }
  catch(IOException ex)
  {
    return false;
  }
    return true;
}

  public boolean saveRoutes(String fileName)
  {
    return false;
  }

  public Set<String> retrieveCityNames()
  {
    return hs;
  }
  
  public boolean addRoute(String source, String destination, int distance,double price) throws CityNotFoundException
  {
    try
    {
      for (int i = 0; i < G.v; i++) 
      {
        for (Route e : G.adj(i)) 
        {
          if((e.source.equals(source) && e.destination.equals(destination))) 
          {
            return false;
          }
         }
      }

    for (int i = 0; i < G.v; i++) 
    {
        for (Route t : G.adj(i)) 
        {
          if((t.source.equals(destination) && t.destination.equals(source)))
          {
            return false;
          }
        }
    }
      
    G.addEdge(new Route(destination, source, distance, price));
    G.addEdge(new Route(source, destination, distance, price));
      
    return true;
  }
  catch(NullPointerException ex)
  { 
    return false;
  }
 }

public Set<Route> retrieveDirectRoutesFrom(String city) throws CityNotFoundException
{
  return new HashSet<Route>(); 
}

public Set<ArrayList<Route>> cheapestItinerary(String source, String destination) throws CityNotFoundException
{
  return new HashSet<ArrayList<Route>>(); 
}

public Set<ArrayList<Route>> cheapestItinerary(String source, String transit, String destination) throws CityNotFoundException
{
  return new HashSet<ArrayList<Route>>(); 
}

public Set<Set<Route>> getMSTs()
{
  return new HashSet<Set<Route>>(); 
}

public Set<ArrayList<Route>> tripsWithin(String city, double budget) throws CityNotFoundException 
{
  return new HashSet<ArrayList<Route>>(); 
}

public Set<ArrayList<Route>> tripsWithin(double budget)
{
  return new HashSet<ArrayList<Route>>(); 
}

public boolean deleteRoute(String source, String destination)throws CityNotFoundException
{
   return true;
}

public void deleteCity(String city) throws CityNotFoundException
{
}

private class Digraph 
  {
    private final int v;
    private int e;
    private LinkedList<Route>[] adj;
    private boolean[] marked;  
    private int[] edgeTo;      
    private int[] distTo;      
    private double[] rate;

    public Digraph(int v) 
    {
      if (v < 0) throw new RuntimeException("Number of vertices must be nonnegative");
      this.v = v;
      this.e = 0;
      @SuppressWarnings("unchecked")
      LinkedList<Route>[] temp = (LinkedList<Route>[]) new LinkedList[v];
      adj = temp;
      for (int i = 0; i < v; i++)
        adj[i] = new LinkedList<Route>();
    }


    public void addEdge(Route edge) 
    {
      int from = Arrays.asList(cityNames).indexOf(edge.source);
      adj[from].add(edge);
      e++;
    }
    
    public Iterable<Route> adj(int v) 
    {
      return adj[v];
    }

    public void bfs(int source) 
    {
      marked = new boolean[this.v];
      distTo = new int[this.e];
      edgeTo = new int[this.v];
      rate = new double[this.e];

      Queue<Integer> q = new LinkedList<Integer>();
      for (int i = 0; i < v; i++)
      {
        distTo[i] = INFINITY;
        rate[i] = 200000.00;
        marked[i] = false;
      }
      distTo[source] = 0;
      rate[source] = 0.0;
      marked[source] = true;
      q.add(source);

      while (!q.isEmpty()) 
      {
        int v = q.remove();
        for (Route w : adj(v)) 
        {
          if (!marked[Arrays.asList(cityNames).indexOf(w.destination)]) {
            edgeTo[Arrays.asList(cityNames).indexOf(w.destination)] = v;
            distTo[Arrays.asList(cityNames).indexOf(w.destination)] = distTo[v] + 1;
            rate[Arrays.asList(cityNames).indexOf(w.destination)] = rate[v] +1;
            marked[Arrays.asList(cityNames).indexOf(w.destination)] = true;
            q.add(Arrays.asList(cityNames).indexOf(w.destination));
          }
        }
      }
    }

    public void dijkstras(int source, int destination) 
    {
      marked = new boolean[this.v];
      distTo = new int[this.v];
      edgeTo = new int[this.v];
      rate = new double[this.v];

      for (int i = 0; i < v; i++)
      {
        distTo[i] = INFINITY;
        rate[i] = 2000000.0;
        marked[i] = false;
      }
      
      distTo[source] = 0;
      rate[source] = 0;
      marked[source] = true;
      int nMarked = 1;
      int current = source;
      
      while (nMarked < this.v) 
      {
        for (Route o : adj(current)) 
        {
          if (distTo[current]+ o.distance < distTo[Arrays.asList(cityNames).indexOf(o.destination)]) 
          {
            edgeTo[Arrays.asList(cityNames).indexOf(o.destination)] = current;
            distTo[Arrays.asList(cityNames).indexOf(o.destination)] = distTo[current] + o.distance;
            rate[Arrays.asList(cityNames).indexOf(o.destination)] = rate[current] + o.price;

          }
        }
        
        int min = INFINITY;
        current = -1;
 
        for(int i = 0; i < distTo.length; i++)
        {
          if(marked[i])
          {
            continue;
          }
          if(distTo[i] < min)
          {
            min = distTo[i];
            current = i;
          }
        }
          if(current < 0) 
          {
            break;
          }
          else
          {
            nMarked++;
            marked[current] = true;
          }
          
      }
    }
  }
}
