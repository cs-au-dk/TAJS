/**
 * TAJS Inspector.
 * <p>
 * {@link dk.brics.tajs.monitoring.inspector.datacollection.InspectorFactory} creates the various data collecting monitors
 * and passes them to an {@link dk.brics.tajs.monitoring.inspector.datacollection.monitors.InspectorMonitor} that
 * starts an {@link dk.brics.inspector.server.InspectorServer} after the analysis is completed.
 * <p>
 * {@link dk.brics.tajs.monitoring.inspector.api.TAJSInspectorAPI} implements the API that provides data for the
 * {@link dk.brics.inspector.server.InspectorServer}, based on information stored in
 * {@link dk.brics.tajs.monitoring.inspector.datacollection.InspectorData} by the data collection monitors.
 */
package dk.brics.tajs.monitoring.inspector;