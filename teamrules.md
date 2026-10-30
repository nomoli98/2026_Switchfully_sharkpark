Teamrules

# Project rules

This document outlines the standard requirements that every Product Backlog Item (PBI), User Story, or Bug must meet before it can be marked as **Done**.

## Team rules

- Daily stand up after group daily stand up.
- Rotating scrum master every day.
- ownership: the devs that are working on the story are the owners of the story.
- Pairing: If we want to for harder parts. 
- Review happens in group and AI is sometimes part of that group.
- Don't touch/change code from colleague without consent.
- Merge conflicts: Will be checked and fixed together in group during code review.
- commit convention: 
  - feat..

## Discuss the project
- Domain modeling: Will be done before moving on to coding. Will be template for the project.
    - Can be modified with entire group agreement.


## Functional tasks and Non-functional tasks

### functional tasks
- technical grooming: is done in team.


### non-functional task
- Github actions will be assisted by mentors
- communication channels: Slack channel and zoom breakout rooms


## Project workflow / Github Issues
### I. Backlog
- Everything that's not for this sprint. Tasks to be defined later.

### II. Ready
- Tasks that are available to do today.

### III. In progress
- Tasks being actively executed and tested.
- Assignee will be appointed

### IV. In review
- Ready for team review.
- Review is always with entire team.
- Reviews are done after daily standup and after lunch.


### V. Done
- When entire team agrees and merges.

## Features / stories
- Sort the features according to prioritize.
- Every feature is a new branch, when code reviewed this will be pushed and merged.


## Coding standards
- Classes:
    - Exception handling: test every case.
    - Error handling: Implement every response annotation.
    - log: important warnings.

- Frontend:
    - Always receives DTO's.
    - DTO's get handled in the service.

- Controller: contains as little business logic as possible
- Service: Connects mapper/dto to entity and pass to repository.
- Repository: Connects with "database";
    - database will consist of complicated stuff.

## filenaming convention (dto's but prettier)
- Everything coming in is a request...
- everything going out is a response. 


## DOD

- Connected to swagger
- Logging in repo and controller
  - Loggers: 
      - Info for repo and controller.
      - Error for exceptions and errors.
- Test coverage of 80%. 


### Testing

- Unit tests as much as possible. 
- integration tests as much as possible.







