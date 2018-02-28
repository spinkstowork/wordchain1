package com.sdp.wordchain1;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Written By: Scott D. Pinkston Feb, 2018
 * Contains an app to be started via the IDE or the command line.
 */
public class Toplevel3 {

    public static void main( String[] args ) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");

        SolutionCoordinator coordinator = ctx.getBean( "solutionCoordinator", SolutionCoordinator.class );
        coordinator.init( ctx );

        // todo: One could set these from command line args, simply hard code for now
        coordinator.solve( "sail", "loft" );
    }
}
