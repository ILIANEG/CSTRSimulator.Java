This is a final project for a computer aided engineering simulation couse. 
##  Capabilities and Implementations
Implementations include:
- Uncontrolled transient state CSTR simulation (constant volume and isothermal operating conditions)*
- Controlled transient state CSTR simulation (constant volume and isothermal operating conditions, PI controller)*
- PI Controller implementation
- ODE solver, with adaptable, bound step size (only RK45 implementation is provided)
(*) Note: additional assumptions applicable, including but not limited to ideal mixing.

## Abstractions
- Abstractions are provided for ODESolver. Should be general enough such that any RK family solver algorithms can be easily adaptable.
- Abstractions provided for control algorithms
- Abstractions provided for reactors (quite general, therefore usecases might be limited, al ot of spcificity must be provided)

## Take away
Some parts of code might be generaly useful (especially control system, which implemented with dead time and polling time), since it can be adapted for any  PID controller.
However, excercise caution, since not all parts of the code were properly tested (in fact, no unit testing was performed due to time constrains of the project, and I am moving on to development
of more comprehensive simulation tools for chemical engineering students, so no further develpment will take place).
Additionally, code destributed under permissive MIT license, so you may use it as you wish. I personally do not reccomend to use
this code as part of your final projects, unless reusing 3rd part source code is allowed by your iunstructor. Take it as some code that can be useful for your own
simulations for educational purposes.
