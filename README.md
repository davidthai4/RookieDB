# CS186 Database Management System

## Overview

This project implements a complete relational database management system (DBMS) as part of UC Berkeley's CS186 course. The system includes all core database components from storage management to query optimization, concurrency control, and crash recovery.

## Project Architecture

The database system is built incrementally across five projects, each adding essential functionality:

### Project 1: Storage Management
- **Buffer Pool Management**: LRU-based buffer replacement policy
- **Disk Space Management**: Page allocation and deallocation
- **Record Management**: Tuple storage and retrieval
- **Heap File Organization**: Sequential file storage

### Project 2: Query Processing
- **Query Execution**: Iterator-based query processing model
- **Join Algorithms**: Implementation of multiple join strategies
  - Nested Loop Join
  - Sort-Merge Join
  - Hash Join
- **Sorting**: External merge sort for large datasets
- **Aggregation**: Group-by operations with hash-based aggregation

### Project 3: Indexing
- **B+ Tree Implementation**: Complete B+ tree index structure
- **Index Operations**: Insert, delete, and search operations
- **Multi-level Indexing**: Support for composite keys
- **Index Maintenance**: Automatic index updates during data modifications

### Project 4: Concurrency Control
- **Lock Management**: Two-phase locking (2PL) implementation
- **Deadlock Detection**: Wait-for graph analysis
- **Transaction Isolation**: Serializable transaction execution
- **Lock Granularity**: Table-level and page-level locking

### Project 5: Recovery (ARIES)
- **Crash Recovery**: ARIES algorithm implementation
- **Transaction Rollback**: Compensation log records (CLRs)
- **Checkpointing**: Fuzzy checkpointing for performance
- **Write-Ahead Logging**: WAL protocol implementation

## Key Components

### Storage Layer
```
BufferManager          # Buffer pool with LRU replacement
DiskSpaceManager       # Page allocation and management
HeapFile              # Sequential file organization
```

### Query Processing
```
QueryExecutor         # Query execution engine
JoinOperator          # Join algorithm implementations
SortOperator          # External merge sort
AggregateOperator     # Group-by operations
```

### Indexing
```
BPlusTree            # B+ tree index implementation
IndexManager          # Index maintenance and operations
```

### Concurrency Control
```
LockManager          # Lock acquisition and release
DeadlockDetector     # Wait-for graph analysis
TransactionManager   # Transaction lifecycle management
```

### Recovery
```
ARIESRecoveryManager # ARIES crash recovery algorithm
LogManager           # Write-ahead logging
LogRecord            # Log record serialization
```

## Implementation Highlights

### Query Optimization
- **Join Selection**: Cost-based join algorithm selection
- **Index Usage**: Automatic index selection for queries
- **Sort Optimization**: External sorting for large datasets
- **Memory Management**: Efficient buffer pool utilization

### Performance Optimizations
- **B+ Tree Efficiency**: O(log n) index operations
- **Join Algorithms**: Optimized for different data distributions
- **Concurrent Processing**: Multi-threaded transaction execution
- **Recovery Speed**: Fuzzy checkpointing reduces restart time

### ACID Guarantees
- **Atomicity**: All-or-nothing transaction execution
- **Consistency**: Database constraints maintained
- **Isolation**: Serializable transaction execution
- **Durability**: Crash recovery ensures data persistence

## Technical Features

### Advanced Join Algorithms
- **Hash Join**: O(n) complexity for equi-joins
- **Sort-Merge Join**: Efficient for pre-sorted data
- **Nested Loop Join**: Simple but effective for small datasets
- **Join Optimization**: Automatic algorithm selection based on data characteristics

### Indexing Strategy
- **B+ Tree Structure**: Balanced tree with high fan-out
- **Range Queries**: Efficient range scan operations
- **Composite Keys**: Multi-column index support
- **Index Maintenance**: Automatic updates during data modifications

### Concurrency Control
- **Two-Phase Locking**: Ensures serializable execution
- **Deadlock Prevention**: Wait-for graph analysis
- **Lock Escalation**: Dynamic lock granularity adjustment
- **Transaction Isolation**: Serializable transaction execution

### Recovery System
- **ARIES Algorithm**: State-of-the-art crash recovery
- **Compensation Log Records**: Efficient transaction rollback
- **Fuzzy Checkpointing**: Non-blocking checkpoint operations
- **Write-Ahead Logging**: Durability guarantee implementation

## Performance Characteristics

### Query Performance
- **Indexed Queries**
- **Join Optimization**
- **Concurrent Processing**: Linear scalability with transaction count
- **Recovery Time**: Sub-second restart for typical workloads

### Memory Efficiency
- **Buffer Pool**: LRU-based page replacement
- **External Sorting**: Memory-efficient large dataset processing
- **Index Caching**: Frequently accessed index pages cached
- **Log Compression**: Efficient log record serialization

## Usage Examples

### Basic Query Execution
```java
// Execute a simple SELECT query
QueryExecutor executor = new QueryExecutor(bufferManager);
ResultSet result = executor.execute("SELECT * FROM users WHERE age > 25");
```

### Index Operations
```java
// Create and use B+ tree index
BPlusTree index = new BPlusTree(bufferManager, schema);
index.insert(key, record);
List<Record> results = index.search(key);
```

### Transaction Management
```java
// ACID transaction execution
Transaction txn = transactionManager.beginTransaction();
try {
    // Perform operations
    txn.commit();
} catch (Exception e) {
    txn.abort();
}
```

## Testing and Validation

The implementation includes comprehensive testing for:
- **Unit Tests**: Individual component functionality
- **Integration Tests**: End-to-end query execution
- **Concurrency Tests**: Multi-threaded transaction scenarios
- **Recovery Tests**: Crash and restart scenarios
- **Performance Tests**: Query optimization validation

## Dependencies

- **Java 8+**: Core language requirements
- **JUnit**: Testing framework
- **Berkeley CS186 Framework**: Course-specific components

---

*This project represents a complete implementation of a relational database management system, covering all fundamental database concepts from storage to recovery.* 