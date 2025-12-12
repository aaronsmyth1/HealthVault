import PatientList from "@/pages/PatientList";

function App() {
  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
      <header className="text-center p-6">
        <h1 className="text-3xl font-bold">Patient Dashboard</h1>
      </header>
      <main>
        <PatientList />
      </main>
    </div>
  );
}

export default App;
