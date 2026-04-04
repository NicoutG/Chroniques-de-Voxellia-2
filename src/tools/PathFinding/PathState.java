package tools.PathFinding;

public enum PathState {
    SEARCHING, // The path is currently being searched for
    FOUND,   // A path has been found and is ready to be used
    REACHED, // The destination has been reached following the path
    FAILED, // The pathfinding process failed to find a path within the search limits
    NO_PATH, // The pathfinding process completed but no valid path was found
}
