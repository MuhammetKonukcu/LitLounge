import BackgroundTasks
import ComposeApp

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(
      _ application: UIApplication,
      didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
      BGTaskScheduler.shared.register(
        forTaskWithIdentifier: "com.muhammetkonukcu.litlounge",
        using: nil
      ) { task in
        self.handleEveningTask(task: task as! BGAppRefreshTask)
      }
      scheduleEveningTask()
      return true
    }

    func scheduleEveningTask() {
      let req = BGAppRefreshTaskRequest(identifier: "com.muhammetkonukcu.litlounge")
      var next20 = Calendar.current.date(bySettingHour: 20, minute: 0, second: 0, of: Date())!
      if next20 <= Date() {
          next20 = Calendar.current.date(byAdding: .day, value: 1, to: next20)!
      }
      req.earliestBeginDate = next20
      try? BGTaskScheduler.shared.submit(req)
    }

    func handleEveningTask(task: BGAppRefreshTask) {
      scheduleEveningTask()
      let queue = OperationQueue()
      queue.maxConcurrentOperationCount = 1

      let op = BlockOperation {
          NotificationManager().showNotification(
              title = "📖 Bugün Ne Okudun?",
              description = "Hedefine yaklaştın mı, bir sayfa daha eklesek nasıl olur?"
          )
      }

      task.expirationHandler = { queue.cancelAllOperations() }
      op.completionBlock = { task.setTaskCompleted(success: !op.isCancelled) }
      queue.addOperation(op)
    }
}
