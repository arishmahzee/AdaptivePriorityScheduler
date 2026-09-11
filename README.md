# Adaptive Priority Scheduler

### Project: In Progress

## Summary

A task scheduler that automatically re-ranks your tasks, factoring in both deadline pressure and financial cost of delay, instead of leaving you to manually reorder a static list every time your day changes.

A Java service tracks task state and computes priority, calling out to a Python microservice that uses a regression model to predict how long a task will actually take. 

Sitting above both is a rule-based Agent that checks in on task state, flags risk, and suggests actions like splitting, deferring, or reprioritizing a task, explaining its reasoning in plain language along the way.
## Why I'm building this
I've always been pretty good with deadlines. But the more I paid attention, the more I realized that's actually not that common, and it's not really about discipline, it's about how people's brains estimate time, both how long something will take, and how much it actually matters if it slips. 

I'm not a neuroscience student or anything, just someone who's genuinely interested in how the brain works and how it messes with us in small daily ways. One thing that fascinates me is how bad we are at estimating how long things take. Someone thinks "this'll take me 20 minutes" and it becomes 45. Then that 45 stretches into their whole afternoon and suddenly the whole day feels ruined, even though realistically only one thing actually took longer than predicted, and often the thing that ate the day wasn't even the most urgent one, just the one that felt loudest.

I think a lot of people quietly hate to-do list apps because of this. The app isn't the problem, the estimation is. The list just sits there being static while your brain burns decision capacity figuring out what to do next, how long it'll take, and what it costs if it slips. So this is my attempt at taking some of that load off, not by nagging you, but by learning how you actually work, tracking the gap between what you plan and what you do, and adjusting priority around you instead of leaving you to fix it manually every time you fall behind.

## The cost-of-delay problem

Partway through building this, I also started thinking about urgency
differently. Deadline pressure isn't the only thing that makes a task
matter, sometimes a task is technically not the most urgent by date, but
delaying it costs more, whether that's money, a client relationship, or a
missed opportunity. So alongside the deadline-based slack calculation, the
scheduler now also considers a rough cost-of-delay for tasks that have one,
so the system isn't just asking "what's due soonest" but "what's due soonest
*and* what does it actually cost if this slips."

On an individual level, I wanted to build this for myself too. Even people
who are good with time still carry a lot of that mental math around in their
head all day. I wanted something that carries a bit of it for me.


## Where this is at right now

Currently through a planned 21 days of build work, spread across three loose phases rather than one continuous sprint. The core services exist and talk to each other end to end, and this last stretch has been about making the whole thing feel like something you could actually run and demo, not just something that works if you know exactly which terminal window to open.

Right now, the Java side is being built in IntelliJ and the Python microservice in VS Code, with the two currently interacting through Postman rather than containerized networking.

Honestly, the best part of coming back to this after a bit of a project block has been enjoying the process again. Learning how everything fits together and treating it like a hobby project instead of something rushed is what I've enjoyed most. I still keep loose internal milestones for myself, an early version, a middle version, a more complete version, but building it this way means I actually get to sit with the small technical decisions instead of skipping past them.

## Done so far

- Task model, dependency tracking, and slack calculation (deadline minus effort minus dependency time)
- A priority queue that automatically re-sorts whenever a task is added, edited, or completed, ranked by slack
- The Python microservice skeleton wired up and called successfully from Java
- A regression model trained on synthetic data that predicts task duration using category, not something hardcoded

## In progress

- Procrastination flagging and completion logging, so actual time versus planned time can be tracked (Day 5 work, not yet finished)

## Not done yet
- Cost-of-delay as a factor in prioritization, decision-making, or agent reasoning
- The day replanner agent itself (state checker, decision engine, action limiter, reasoning generator), entirely
- A real database. Task and history storage is currently in-memory or file-based only
- Any automation or scheduled notification layer
- The Agent is a rule-based decision layer that checks task state, pulls in relevant data, and decides on actions like splitting, deferring, or flagging a task, capped by a daily action limit, with no LLM involved until much later, when one gets added purely to phrase its reasoning in natural language.
- An LLM call wired into the Agent's decision engine to phrase its reasoning naturally (the agent's actions would still be rule-based; this only affects how it explains itself)
- Containerization with Docker or docker-compose
- A proper frontend
- Cloud deployment
- Fuller test coverage

## How it's structured

Java owns the task state and does the ranking, now on both slack and cost. Python is stateless and only gets called when Java needs a duration estimate. The agent sits above both, reading task state and predictions, reasoning over them - including financial trade-offs where relevant - and pushing suggested changes back to Java. Everything now runs inside containers rather than as separate local processes, with Java reaching Python by its service name instead of localhost.


Future Work: ML-Predicted Cost of Delay

Cost-of-delay is currently a manually-tagged or formula-based field (e.g. hourly rate × hours delayed), not something the system predicts the way it already does for task duration. That's a deliberate scoping call: a second ML model needs a labelled dataset of actual cost outcomes that doesn't exist yet, whereas duration data is already being logged through normal task completion. The natural next step is training a lightweight regression model on category and historical delay patterns to predict cost-of-delay the same way DurationPredictor predicts hours, moving it from a static number to a genuinely predictive feature.


I'm excited to finish this soon and learn the most from it before moving onto my next project.