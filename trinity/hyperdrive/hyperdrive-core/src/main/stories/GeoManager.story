Narrative:
In order to manage child's geometry, a geomanager must be set on the child's parent.
The geomanager will respond on a child's geometry requests.
As a programmer
I want a geomanager that manages child widgets geometry
so I don't have to implement the same geometry rules again for every case

Scenario:  A child requests a new size and place to a direct geomanager.
 
Given a geovirtrectangle parent with a direct geomanager
And a geovirtrectangle child
When the child sets a new horizontal position
And the child sets a new vertical position
And the child sets a new width
And the child sets a new height
And the child requests to update it's geometry
Then the child should be updated to the new horizontal position
And the child should be updated to the new new vertical position
And the child should be updated to the new width
And the child should be update to the new height

And the child is moved to the new position

Scenario:  A child requests a new size to a direct geomanager.
 
Given a geovirtrectangle parent with a direct geomanager
And a geovirtrectangle child
When the child sets a new width
And the child sets a new height
And the child requests to update it's size
Then the child should be updated to the new width
And the child should be update to the new height
 
Scenario:  A child request a new place to a direct geomanager.
 
Given a geovirtrectangle parent with a direct geomanager
And a geovirtrectangle child
When the child sets a new horizontal position
And the child sets a new  vertical position
And the child requests to update it's position
Then the child should be updated to the new horizontal position
And the child should be updated to the new vertical position