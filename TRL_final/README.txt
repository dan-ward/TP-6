Textbook Rental Library
SEIS635: Software Analysis & Design (Fall 2017)

Dan Ward: 		https://github.com/dan-ward
Eric Helander: 	https://github.com/hela6565
	(Eric switched to his work computer for TP-6 and beyond in order to install Eclipse Oxygen. Commits, however, ended up attached to his work account - EHelander - rather than hela6565.)

GitHub Repository: https://github.com/dan-ward/TP-6

After importing the project into Eclipse, navigate to TRL_final > PresentationLayer > (default package) > TRLApp, and run as a Java Application. Once running, the user is guided through available actions and options via console output and input.

We implemented core functionality for a Worker to check multiple Copies out to a Patron and to check in multiple Copies from a Patron. The user may examing a Patron or Copy apart from checking Copies in or out (transaction type = lookup > [Patron|Copy).

The additional features we implemented include the following:
	Generating Holds for Patrons with overdue Copies (transaction type = hold)
	Generate OverdueNotices for Patrons with Holds for overdue Copies (transaction type = notice)
	Print OverdueNotices (transaction type = notice)
	Recording state change Events in the Log
	Searching through the Event Log (transaction type = lookup > eventLog)