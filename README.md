## Inspiration
Imagine you have an event at home and you want to know where are your guests, and also remember them not to arrive late.

## What it does
With a simple control panel the host can manage his event and check which guests are coming. When the host requires it, the system sends and SMS to the guests reminding them about the meeting. This SMS activates an Android Service which sends the confirmation and the guests position to the host. That results are shown in a list and also in a map.

## How we built it
To achieve this we made :
- A backend server API in Flask, that also uses the Twilio API to send the SMS.
- A frontend server in Angular 2, that shows the control panel to the hosts.
- An Android native Application/Service to send the host the required information.

## Challenges we ran into
It was our first time using Flask and Twilio. We wanted to know how to use them and... It's pretty easy and cool!

## Accomplishments that we're proud of
Making it work in less than 24h. As we are a team of 3, it was a huge amount of work to do, so, we divided one develop part for each one so we have been able to work simultaneusly in diferent areas. At the end, all the 'parts' of the application are working so we are proud of it!

## What we learned
What is it and how does Flask work. Also we learned how does Twilio work. 

## What's next for TrackMyGuests
We are... just waiting some company to fund us, because it has a lot of chances to be exploited.
