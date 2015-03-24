# TraSMAPI (Traffic Simulation Manager Application Programming Interface)

TraSMAPI can be seen as a generic API for microscopic traffic that allows real-time communication between agents of urban traffic management (such as vehicles and traffic signals) and the environment created by various simulators. This tool was developed in LIACC (Artificial Intelligence and Computer Science Laboratory), University of Porto.

This API offers a higher abstraction level than most of microscopic traffic simulators in such a way that the solution is independent from the microscopic simulator to use.



## Current supported microscopic simulators

Next, there are the current simulators that TraSMAPI support. If you want to extend TraSMAPI to other simulators feel free to do so, forking us and making a pull request after.

#### SUMO
SUMO is an open-source program (licenced under GPL) for traffic simulation. Its simulation model is microscopic, that is, each vehicle is explicitly modeled, has its own route and moves individually over the network. It is mainly developed by Institute of Transportation Systems, located at German Aerospace Center.

**Useful links:**

* [Official website](http://www.sumo-sim.org)
* [Some simple tutorials (in portuguese)](http://www.galirows.com.br/meublog/blog/tag/sumo/)



## Published papers



[TraSMAPI: An API Oriented Towards Multi-Agent Systems RealTime Interaction with Multiple Traffic Simulators](http://www.cs.cmu.edu/~maraujo/papers/itsc10.pdf)

[Using TraSMAPI for Developing Multi-Agent Intelligent Traffic Management Solutions](http://link.springer.com/chapter/10.1007%2F978-3-642-19875-5_16)

[Using TraSMAPI for the Assessment of Multi-Agent Traffic Management Solutions](http://link.springer.com/article/10.1007%2Fs13748-012-0013-y)

[An Integrated Framework for Multi-Agent Traffic Simulation using SUMO and JADE](http://paginas.fe.up.pt/~niadr/PUBLICATIONS/2013/SUMO2013b.pdf)

[JADE, TraSMAPI and SUMO: A tool-chain for simulating traffic light control](http://agents.fel.cvut.cz/att2014/att2014_paper_16.pdf)

## Contributors
So far, many people contributed in the development of TraSMAPI: Ivo Timóteo, Miguel Araújo, Guilherme Soares, José Macedo, Tiago Azevedo, Paulo Araújo, Zafeiris Kokkinogenis and Rosaldo Rossetti.



## Start using it!
The folder *simple-examples* has two code examples that could help you in a first approach to TraSMAPI. They were last tested with SUMO 0.18.0. We are currently working in more suitable tutorials for TraSMAPI, but any help would be very appreciated.

Current documentation and tutorials could be seen in our [Wiki Page](https://github.com/tjiagoM/TraSMAPI/wiki)
