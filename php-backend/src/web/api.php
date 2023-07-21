<?php
header('Content-Type: application/json'); //Set the response content type to JSON

//Split the request path by slashes
$pathInfo = explode('/', $_SERVER['PATH_INFO']);

//Check if the path is valid (contains at least two elements)
if (count($pathInfo) < 2) {
    echo json_encode([
        'error' => 'Invalid path'
    ]);
    exit;
}

//Connect to the PostgreSQL database
$dbconn = pg_connect("host=db dbname=postgres user=postgres password=postgres");

//Get the resource from the path
$resource = $pathInfo[1];

//Handle different resources based on the request
switch ($resource) {
    case 'members':
        //If the resource is 'members', fetch all memberships from the database
        $result = pg_query_params($dbconn, 'SELECT * FROM memberships', []);
        $memberships = [];
        while($membership = pg_fetch_assoc($result)) {
            $memberships[] = $membership;
        }
        echo json_encode([
            'memberships' => $memberships
        ]);
        break;
    case 'users':
        //If the resource is 'users', check if a specific user ID is provided in the path
        if (isset($pathInfo[2])) {
            $userId = $pathInfo[2];
            //Fetch the user with the provided ID from the database
            $result = pg_query_params($dbconn, 'SELECT * FROM users WHERE id = $1', [$userId]);
            $user = pg_fetch_assoc($result);
            echo json_encode($user);
        } else {
            //If no specific user ID is provided, fetch all users from the database
            $result = pg_query_params($dbconn, 'SELECT * FROM users', []);
            $users = [];
            while($user = pg_fetch_assoc($result)) {
                $users[] = $user;
            }
            echo json_encode([
                'users' => $users
            ]);
        }
        break;
    default:
        // If the resource is not 'members' or 'users', return an error
        echo json_encode([
            'error' => 'Invalid resource'
        ]);
        break;
}
?>
