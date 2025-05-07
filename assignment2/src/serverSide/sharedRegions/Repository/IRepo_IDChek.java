package serverSide.sharedRegions.Repository;

/**
 * The IRepo_IDChek interface contains the methods that a repository should implement to interact with the ID check shared region.
 * The repository shared region interacts with the ID check shared region to log the ID check results of voters.
 */
public interface IRepo_IDChek {
    /**
     * The logIDCheck method logs the ID check results of a voter in the ID check shared region.
     * @param voterId The voter's ID.
     * @param accepted A char that indicates if the voter's ID was accepted or not.
     */
    void logIDCheck(String voterId, char accepted);
}
