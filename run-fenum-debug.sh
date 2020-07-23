#!/bin/bash

ROOT=$(cd $(dirname "$0")/.. && pwd)

CFI=$ROOT/checker-framework-inference

AFU=$ROOT/annotation-tools/annotation-file-utilities
export PATH=$AFU/scripts:$PATH

CHECKER=fenum.FenumInfChecker

#SOLVER=fenum.solver.FenumSolverEngine
#IS_HACK=true

 DEBUG_SOLVER=checkers.inference.solver.DebugSolver
 SOLVER="$DEBUG_SOLVER"
# IS_HACK=false
 IS_HACK=true
 DEBUG_CLASSPATH=""

FENUMPATH=$ROOT/fenum-demo/build/classes/java/main
export CLASSPATH=$FENUMPATH:$DEBUG_CLASSPATH:.
export external_checker_classpath=$FENUMPATH

CFI_LIB=$CFI/lib
export DYLD_LIBRARY_PATH=$CFI_LIB
export LD_LIBRARY_PATH=$CFI_LIB

$CFI/scripts/inference-dev --checker "$CHECKER" --solver "$SOLVER" --solverArgs="collectStatistics=true" --hacks="$IS_HACK" --logLevel=FINER -m ROUNDTRIP -afud ./annotated-debug "$@"

# TYPE CHECKING
# $CFI/scripts/inference-dev --checker "$CHECKER" --solver "$SOLVER" --solverArgs="collectStatistics=true,solver=z3" --hacks="$IS_HACK" -m TYPECHECK "$@"
