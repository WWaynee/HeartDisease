package testing;

import mapreduce.jobs.GetCount;
import mapreduce.jobs.Hyperglycemia;
import mapreduce.jobs.Hyperlipemia;
import mapreduce.jobs.Hypertension;
import mapreduce.jobs.MainBloodVessels;
import mapreduce.jobs.THAL;

public class testAll {
  public static void main(String[] args) throws Exception {
    GetCount.Run();
    Hyperglycemia.Run();
    Hypertension.Run();
    THAL.Run();
    Hyperlipemia.Run();
    MainBloodVessels.Run();
  }
}