package org.fulib.reacher;

import org.fulib.builder.ClassModelBuilder;

public class ReachabilityGraphModel
{
   public static void main(String[] args)
   {
      ClassModelBuilder mb = new ClassModelBuilder("org.fulib.reacher.reachabilty");

      mb.buildClass("ReachabilityGraph");
   }
}
