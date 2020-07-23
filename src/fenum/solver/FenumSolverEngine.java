package fenum.solver;

import checkers.inference.solver.SolverEngine;
import checkers.inference.solver.backend.SolverFactory;
import checkers.inference.solver.backend.maxsat.MaxSatSolverFactory;

public class FenumSolverEngine extends SolverEngine {

    @Override
    protected SolverFactory createSolverFactory() {
        return new MaxSatSolverFactory();
    }
}
