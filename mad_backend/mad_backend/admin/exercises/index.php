<?php 
	require_once('../../mysql_connect.php'); 
?> 

<html>
<head>
	<title>admin/exercises</title>
</head>
<body>

	<h1>Exercises</h1>
	<?php

		// Create connection
		$conn = connect_db();
		// Check connection
		if ($conn->connect_error) {
		    die("Connection failed: " . $conn->connect_error);
		} 

		$sql = "SELECT id, name, description, image, target, musclegroup FROM mad_exercises";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		    // output data of each row
		    while($row = $result->fetch_assoc()) {
		        echo "id: " . $row["id"]. " - Name: " . $row["name"]. " - Description: " . $row["description"]. " - image: " . $row["image"] . " - target: " . $row["target"] . " - musclegroup: " . $row["musclegroup"] ."<br>"; 
		    }
		} else {
		    echo "0 results";
		}
		$conn->close();
	?>
</body>
</html>