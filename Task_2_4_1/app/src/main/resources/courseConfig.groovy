tasks = [
    [
        id: 'Task_1_1',
        name: 'Task_1_1',
        maxPoints: 1,
        softDeadline: '2025-05-10',
        hardDeadline: '2025-05-15'
    ],
    [
        id: 'Task_1_2_2',
        name: 'Task_1_2_2',
        maxPoints: 3,
        softDeadline: '2025-05-20',
        hardDeadline: '2025-05-25'
    ]
]

groups = [
    [
        name: '23216',
        students: [
            [
                githubUsername: 'Repeynik',
                fullName: 'Savushkina Alena',
                repositoryUrl: 'https://github.com/Repeynik/OOP'
            ]
        ]
    ]
]

bonusPoints = [
    [
        student: 'Savushkina Alena',
        task: 'Task_1_1',
        points: 10
    ]
]

checkpoints = [
    [name: 'Midterm', date: '2025-06-01'],
    [name: 'Final', date: '2025-07-01']
]

activityScoring = [
    activeCommitThreshold: 1,
    bonusPointsPerActiveWeek: 1
]

studentsToCheck =[
    [name: "Savushkina Alena"],
    [name: "Savushkina Alena"],
    [name: "Savushkina Alena"]
]

weeklyStartDate = '2025-01-01'

grading = [
    firstSemester: [
        exellent: 13,
        good: 10,
        satisfactory: 7
    ],
    secondSemester: [
        exellent: 5,
        good: 4,
        satisfactory: 3
    ]
]