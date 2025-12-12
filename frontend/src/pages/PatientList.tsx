import { useEffect, useState } from "react";

interface PatientCondition {
  id?: number;
  code?: string;
  display?: string;
  recordedDate?: string;
  clinicalStatus?: string;
  verificationStatus?: string;
}

interface PatientAllergy {
  id?: number;
  substance?: string;
  substanceCode?: string;
  severity?: string;
  manifestation?: string;
  recordedDate?: string;
  clinicalStatus?: string;
}

interface PatientMedication {
  id?: number;
  medicationDisplay?: string;
  medicationCode?: string;
  dosageText?: string;
  status?: string;
  authoredOn?: string;
  intent?: string;
}

interface PatientObservation {
  id?: number;
  code?: string;
  display?: string;
  valueString?: string;
  valueQuantity?: string;
  unit?: string;
  effectiveDateTime?: string;
  status?: string;
}

interface PatientEncounter {
  id?: number;
  type?: string;
  typeDisplay?: string;
  status?: string;
  startTime?: string;
  endTime?: string;
  reason?: string;
  reasonCode?: string;
}

interface Patient {
  id?: number;
  identifier?: string;
  givenName?: string;
  familyName?: string;
  gender?: string;
  birthDate?: string;
  phone?: string;
  email?: string;
  addressLine?: string;
  city?: string;
  postalCode?: string;
  managingOrganisation?: string;
  generalPractitioner?: string;
  conditions?: PatientCondition[];
  allergies?: PatientAllergy[];
  medications?: PatientMedication[];
  observations?: PatientObservation[];
  encounters?: PatientEncounter[];
}


const API_URL = "http://localhost:8080/api/patients";

export default function PatientList() {
const [patients, setPatients] = useState<Patient[]>([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
  const [activeTab, setActiveTab] = useState("overview");

  


  useEffect(() => {
    async function fetchPatients() {
      try {
        const res = await fetch(API_URL);
        if (!res.ok) throw new Error("Failed to fetch patients");
        const data = await res.json();
        setPatients(data);
      } catch (err:any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchPatients();
  }, []);

  const calculateAge = (birthDate?: string): number | string => {
    if (!birthDate) return "N/A";
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) age--;
    return age;
  };

  if (loading) return <p className="text-center mt-10">Loading patients...</p>;
  if (error) return <p className="text-center mt-10 text-red-500">{error}</p>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">Patient Dashboard</h1>

      {/* Patient List */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {patients.map((p) => (
          <div key={p.id} className="border rounded p-4 bg-white shadow-sm">
            <h2 className="font-bold text-lg">{p.givenName} {p.familyName}</h2>
            <p>ID: {p.identifier}</p>
            <p>Age: {calculateAge(p.birthDate)} {p.gender && `(${p.gender})`}</p>
            {p.phone && <p>Phone: {p.phone}</p>}
            {p.email && <p>Email: {p.email}</p>}
            {p.addressLine && <p>Address: {p.addressLine}{p.city ? `, ${p.city}` : ""}</p>}

            <button
              onClick={() => setSelectedPatient(p)}
              className="mt-2 px-3 py-1 bg-blue-500 text-white rounded"
            >
              View Details
            </button>
          </div>
        ))}
      </div>

      {/* Patient Details Modal */}
      {selectedPatient && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white w-full max-w-2xl p-6 rounded shadow-lg overflow-y-auto max-h-[90vh]">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">
                {selectedPatient.givenName} {selectedPatient.familyName}
              </h2>
              <button
                onClick={() => {
                  setSelectedPatient(null);
                  setActiveTab("overview");
                }}
                className="px-2 py-1 bg-gray-300 rounded"
              >
                Close
              </button>
            </div>

            {/* Tabs */}
            <div className="flex gap-2 mb-4">
              {["overview","conditions","allergies","medications","observations","encounters"].map(tab => (
                <button
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`px-3 py-1 rounded ${activeTab === tab ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                >
                  {tab.charAt(0).toUpperCase() + tab.slice(1)}
                </button>
              ))}
            </div>

            {/* Tab Content */}
            {activeTab === "overview" && (
              <div>
                <h3 className="font-bold mb-2">Demographics</h3>
                <p>Gender: {selectedPatient.gender || "N/A"}</p>
                <p>Age: {calculateAge(selectedPatient.birthDate)}</p>
                <p>DOB: {selectedPatient.birthDate ? new Date(selectedPatient.birthDate).toLocaleDateString() : "N/A"}</p>
                <p>Phone: {selectedPatient.phone || "N/A"}</p>
                <p>Email: {selectedPatient.email || "N/A"}</p>
                <p>Address: {selectedPatient.addressLine || ""}{selectedPatient.city ? `, ${selectedPatient.city}` : ""}</p>
              </div>
            )}

            {activeTab === "conditions" && (
              <div>
                <h3 className="font-bold mb-2">Conditions</h3>
                {selectedPatient.conditions && selectedPatient.conditions.length > 0 ? (
                  selectedPatient.conditions.map(c => (
                    <div key={c.id} className="border p-2 mb-2 rounded bg-gray-50">
                      <p>{c.display || c.code}</p>
                      <p>Status: {c.clinicalStatus || "Unknown"}</p>
                    </div>
                  ))
                ) : <p>No conditions recorded</p>}
              </div>
            )}

            {activeTab === "allergies" && (
              <div>
                <h3 className="font-bold mb-2">Allergies</h3>
                {selectedPatient.allergies && selectedPatient.allergies.length > 0 ? (
                  selectedPatient.allergies.map(a => (
                    <div key={a.id} className="border p-2 mb-2 rounded bg-yellow-50">
                      <p>{a.substance}</p>
                      <p>Severity: {a.severity || "Unknown"}</p>
                    </div>
                  ))
                ) : <p>No allergies recorded</p>}
              </div>
            )}

            {activeTab === "medications" && (
              <div>
                <h3 className="font-bold mb-2">Medications</h3>
                {selectedPatient.medications && selectedPatient.medications.length > 0 ? (
                  selectedPatient.medications.map(m => (
                    <div key={m.id} className="border p-2 mb-2 rounded bg-green-50">
                      <p>{m.medicationDisplay}</p>
                      <p>Status: {m.status || "Unknown"}</p>
                    </div>
                  ))
                ) : <p>No medications recorded</p>}
              </div>
            )}

            {activeTab === "observations" && (
              <div>
                <h3 className="font-bold mb-2">Observations</h3>
                {selectedPatient.observations && selectedPatient.observations.length > 0 ? (
                  selectedPatient.observations.map(o => (
                    <div key={o.id} className="border p-2 mb-2 rounded bg-blue-50">
                      <p>{o.display || o.code}</p>
                      <p>Value: {o.valueString || o.valueQuantity || "N/A"} {o.unit || ""}</p>
                    </div>
                  ))
                ) : <p>No observations recorded</p>}
              </div>
            )}

            {activeTab === "encounters" && (
              <div>
                <h3 className="font-bold mb-2">Encounters</h3>
                {selectedPatient.encounters && selectedPatient.encounters.length > 0 ? (
                  selectedPatient.encounters.map(e => (
                    <div key={e.id} className="border p-2 mb-2 rounded bg-purple-50">
                      <p>{e.typeDisplay || e.type}</p>
                      <p>Status: {e.status || "Unknown"}</p>
                    </div>
                  ))
                ) : <p>No encounters recorded</p>}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
