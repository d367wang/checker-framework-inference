package purity.solve.solver;

import checkers.inference.solver.SolverEngine;
import checkers.inference.solver.backend.SolverFactory;
import checkers.inference.solver.backend.maxsat.MaxSatSolverFactory;
import checkers.inference.solver.strategy.SolvingStrategy;

public class PuritySolverEngine extends SolverEngine {
    @Override
    protected SolvingStrategy createSolvingStrategy(SolverFactory solverFactory) {
        return super.createSolvingStrategy(solverFactory);
    }

    @Override
    protected void sanitizeSolverEngineArgs() {
        super.sanitizeSolverEngineArgs();
    }

    @Override
    protected SolverFactory createSolverFactory() {
        return new MaxSatSolverFactory();
    }

}
