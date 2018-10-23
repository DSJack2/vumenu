# VUMenu (+1 615 997 1405)

VUMenu is a new text-based messaging app for providing students with the menus of various dining halls around campus. It works in a very simple way. A user will text the server the name of the campus dining option, and time of day (breakfast, lunch, dinner) and the server responds with all of the items on the menu for that meal period. Entrees, sides, and desserts will be broken down into subcategories and show them in an easy to read format for mobile phones.

From personal experience and the potential users I have interviewed, I believe there is a need for another method for obtaining the daily menus for campus dining halls. The current app often has unexpected crashes and can give incorrect information about what food is available. The full site is hard to use on a mobile phone so the next best option would be using texts. It doesn’t rely on having a data connection and many students are texting frequently anyway.  The excuse “I don’t have the app” is no longer a reason for someone to know what the menu options for the day are.

Some features that are going to be implemented are things such as being able to find vegetarian, gluten-free or vegan options, sending an automated text to users when large specials such as S.S. Rand and Commons Ball. Additionally, users will be able to register their favorite rotating dishes and be notified any time they are available on campus. This way, users will be informed about their favorite meals and be more inclined to stay on campus. 



# Questions
  1.	How do you currently find food options on campus?

  2.	Are you happy with the current method? Why or why not?

  3.	How familiar are you with the hours of the various dining options on campus?

  4.	How often do you check the menus before deciding when/where to eat?

  5.  Does the menu actually play a factor in where you decide to eat?

  6.	What sort of information do you look for when deciding where to eat on campus?

  7.	Is there anything that you would like for the current method to do that it currently does not?

  8.	Where on campus do you currently eat the most?

  9.	Would you be more likely to eat on campus if menus were more accessible?

  10.	How do you hear about campus dining’s special event dinners and lunches?
  
# Answers

## Question 1: 

  A1: I walk into wherever I wanna eat and see what they got. Or by word of mouth
  
  A2: I ask my friends or check the online portal
  
  A3: Although there is variety, I feel like the food options on campus are definitely lacking. Lunch provides a good assortment of options,      particularly at Rand. However for dinner there are hardly any options.
  
## Question 2: 

  A1: More or less. It's fine but could be improved to make menus more easily accessible.
  
  A2: No, the mobile app is terrible and it is not always up to date.
  
  A3: I am not too happy with the current method. I feel as though there are limited options and times for eating, which are tough to abide by with busy schedules.

## Question 3: 

  A1: Pretty familiar but don't know all of them off the top of my head. A quick Google search usually tells me
  
  A2: Not very familiar, there are different opening and closing time for various places on campus.
  
  A3: I know what all the hours are for dining halls on campus.
  
## Question 4: 

  A1: Almost never. I only depend on word of mouth.
  
  A2: I would say that my friends and I check about half the time.
  
  A3: Rarely check the menus.
  
## Question 5: 

  A1: Yes, if I hear there's a special that I like, I'll probably eat there since it can be hard to find variation in campus dining options.
  
  A2: I would say 6 out 10 times it plays a role.
  
  A3: Only for Kissam/EBI.
  
## Question 6: 

  A1: What's being served and when.
  
  A2: How close is it to where I am, type of food, and amount of food I will get.
  
  A3: What time it is and what day of the week.
  
## Question 7: 

  A1: An easier way to know the menu options would be nice. The campus dining app is pretty useless.
  
  A2: Tell me wait times.
  
  A3: Rand’s hours need to be improved, Rand during lunch needs to be faster, meal periods are highly restrictive, more all you can eat options (or at least for certain things i.e. Rand breakfast). Tower’s munchie mart should be 24 hrs on weekends, bring back Chick-Fil-A, better coffee on campus.
  
## Question 8: 

  A1: Rand
  
  A2: Rand
  
  A3: Rand
  
## Question 9: 

  A1: Yes
  
  A2: Yes
  
  A3: No
  
## Question 10: 

  A1: Mainly through word of mouth or the occasional flyer.
  
  A2: My Friends.
  
  A3: Other students and posters.

# Requirements
  Because VUMenu is going to be text-based, some messaging service that has the ability to send and receieve messages from someone's phone.  This application will require some basic information that will have to be obtained from the school. This information being all available campus dining halls, their hours, and the menus from each day. Some sort of database to store the information will be necessary, and as the menus change, the changes should be reflected in said database as well. Additionally, VUMenu will require an automated way to notify users when their favorite menu options become available. In case any new campus dining halls open, VUMenu will require a way to add (or remove) locations. In terms of what the application delivers to users, the information that is being stored for every dining hall needs to be sent in a single message.
  
# Development Approach
  The first step for me in the development process for VUMenu would be to sit down with the potential users of my application and making sure that the requirements I laid out are in line with what they would actually need. This will help prevent working in the wrong direction of what the users are actually looking for in the application. Next, I will have to prioritize the requirements and features that are intended to be implemented. There is a limited amount of time to develop the application, so naturally it would make more sense to have the core functionality working properly before any "bonus" features get added. The biggest challenge I could see coming up is integrating the application with Twilio. If a very small, basic application can be built and a message is successfully sent and recieved from Twilio, I will know how I should be formatting messages that are recieved from users. From there, I want to define the format for the data I need stored (halls, hours, entrees, etc.), so that when it comes time to retrieve or add to the data the process is a bit smoother. From there, I can start writing functions to handle the messages recieved by users. When users subscribe to the service, I will have to provide some sort of instructions on what needs to be contained in the message, and how to format it. 

