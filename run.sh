#!/bin/bash

#ROOT=$(cd $(dirname "$0")/.. && pwd)
ROOT=/home/wd

CFI=$ROOT/checker-framework-inference

AFU=$ROOT/annotation-tools/annotation-file-utilities
export PATH=$AFU/scripts:$PATH

#CHECKER=dataflow.DataflowChecker
#CHECKER=purity.PurityChecker
CHECKER=dummy.DummyChecker

#SOLVER=dataflow.solvers.general.DataflowSolverEngine
#SOLVER=purity.solve.solver.PuritySolverEngine
SOLVER=checkers.inference.solver.DebugSolver
IS_HACK=true

# DEBUG_SOLVER=checkers.inference.solver.DebugSolver
# SOLVER="$DEBUG_SOLVER"
# IS_HACK=true
# DEBUG_CLASSPATH=""

MAINPATH=$CFI/build/classes/java/main
export CLASSPATH=$MAINPATH:$DEBUG_CLASSPATH:.
export external_checker_classpath=$MAINPATH

CFI_LIB=$CFI/lib
export DYLD_LIBRARY_PATH=$CFI_LIB
export LD_LIBRARY_PATH=$CFI_LIB

$CFI/scripts/inference-dev --checker "$CHECKER" --solver "$SOLVER" --solverArgs="collectStatistics=true" --hacks="$IS_HACK" --logLevel WARNING -m ROUNDTRIP -afud ./annotated "$@"

# TYPE CHECKING
# $CFI/scripts/inference-dev --checker "$CHECKER" --solver "$SOLVER" --solverArgs="collectStatistics=true,solver=z3" --hacks="$IS_HACK" -m TYPECHECK "$@"
 #$CFI/scripts/inference-dev --checker "$CHECKER" --solver "$SOLVER" --solverArgs="collectStatistics=true" --hacks="$IS_HACK" -m TYPECHECK "$@"
