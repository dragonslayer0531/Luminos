package tk.luminos.editor.nodes;

import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;
import tk.luminos.graphics.ui.NuklearObject;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nuklear.Nuklear.*;

public class NodeEditor implements NuklearObject {

	public boolean initialized = false;
	public List<EditorNode> node_buffer = new ArrayList<EditorNode>();
	public List<EditorNodeLink> node_links = new ArrayList<EditorNodeLink>();
	public EditorNode begin;
	public EditorNode end;
	public NkRect bounds;
	public EditorNode selected;
	public int show_grid = 0;
	public EditorNodeLinking linking;
	public Vector2 scroll = new Vector2();

	public int ids;

	public int x, y, width, height;
	public String title;

	public NodeEditor(int x, int y, int width, int height, String title) {
		initialized = true;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.title = title;
		this.show_grid = nk_true;
		bounds = NkRect.mallocStack();
		linking = new EditorNodeLinking();
	}
	
	protected void finalize() {
		bounds.free();
	}

	public void layout(NkContext ctx, MemoryStack stack) {
		final NkRect total_space = NkRect.mallocStack(stack);
		NkCommandBuffer canvas = null;
		NkInput input = ctx.input();
		EditorNode updated = null;
		
		if (nk_begin(ctx, title, nk_rect(x, y, width, height, total_space), NK_WINDOW_BORDER|NK_WINDOW_NO_SCROLLBAR|NK_WINDOW_MOVABLE|NK_WINDOW_SCALABLE|NK_WINDOW_CLOSABLE)) {
			canvas = nk_window_get_canvas(ctx);
			nk_window_get_content_region(ctx, total_space);
			nk_layout_space_begin(ctx, NK_STATIC, total_space.h(), node_buffer.size());
			{
				NkRect size = NkRect.mallocStack(stack);
				size = nk_layout_space_bounds(ctx, size);

				if (this.show_grid == nk_true) {
					float x, y;
					final float grid_size = 32;
					final NkColor gridColor = NkColor.mallocStack(stack);
					nk_rgb(50, 50, 50, gridColor);

					for (x = (float) ((size.x()) % grid_size); x < size.w(); x += grid_size) {
						nk_stroke_line(canvas, x + size.x(), size.y(), x + size.x(), size.y() + size.h(), 1.0f, gridColor);
					}
					for (y = (float) ((size.y()) % grid_size); y < size.h(); y += grid_size) {
						nk_stroke_line(canvas, size.x(), y + size.y(), size.x() + size.w(), y + size.y(), 1.0f, gridColor);
					}
				}
				
				NkPanel node = NkPanel.mallocStack(stack);
				for (EditorNode it : node_buffer) {
					NkRect rect = NkRect.mallocStack(stack);
					nk_layout_space_push(ctx, nk_rect(it.bounds.x() - scroll.x, it.bounds.y() - scroll.y, it.bounds.w(), it.bounds.h(), rect));
					if (nk_group_begin(ctx, it.name, NK_WINDOW_NO_SCROLLBAR | NK_WINDOW_BORDER | NK_WINDOW_TITLE))
					{
						node = nk_window_get_panel(ctx);
						if (nk_input_mouse_clicked(input, NK_BUTTON_RIGHT, node.bounds()) && !(it.prev != null && nk_input_mouse_clicked(input, NK_BUTTON_RIGHT, node.bounds().set(nk_layout_space_rect_to_screen(ctx, node.bounds())))))
						{
							updated = it;				
						}
						
						if (linking.active != nk_true && nk_input_is_mouse_down(input, NK_BUTTON_RIGHT) && nk_input_is_mouse_hovering_rect(input, node.bounds())) {
							node.bounds().x(node.bounds().x() + input.mouse().delta().x());
							node.bounds().y(node.bounds().y() + input.mouse().delta().y());
							it.bounds.x(it.bounds.x() + input.mouse().delta().x());
							it.bounds.y(it.bounds.y() + input.mouse().delta().y());
						}
						
						it.layout(ctx);
						nk_group_end(ctx);
					}					
					
					{						
						float space = node.bounds().h() / (float) (it.outputCount + 1);
						for (int i = 0; i < it.outputCount; ++i) {
							NkRect circle = NkRect.mallocStack(stack);
							circle.x(node.bounds().x() + node.bounds().w() - 4);
							circle.y(node.bounds().y() + space * (float) (i + 1));
							circle.w(8);
							circle.h(8);
							nk_fill_circle(canvas, circle, nk_rgb(100, 100, 100, NkColor.mallocStack(stack)));

							if (nk_input_has_mouse_click_down_in_rect(input, NK_BUTTON_LEFT, circle, nk_true)) {
								linking.active = nk_true;
								linking.node = it;
								linking.input_id = it.id;
								linking.input_slot = i;
							}

							if (linking.active == nk_true && linking.node == it && linking.input_slot == i) {
								linking.input_slot = i;
								NkVec2 l0 = NkVec2.mallocStack(stack);
								l0 = nk_vec2(circle.x() + 3, circle.y() + 3, l0);
								NkVec2 l1 = NkVec2.mallocStack(stack);
								l1 = input.mouse().pos();
								nk_stroke_curve(canvas, l0.x(), l0.y(), l0.x() + 50f, l0.y(), l1.x() - 50.f, l1.y(), l1.x(), l1.y(), 1.0f, nk_rgb(100, 100, 100, NkColor.mallocStack(stack)));
							}
						}

						space = node.bounds().h() / (float) (it.inputCount + 1);
						for (int i = 0; i < it.inputCount; ++i) {
							NkRect circle = NkRect.mallocStack(stack);
							circle.x(node.bounds().x() - 4);
							circle.y(node.bounds().y() + space * (float) (i + 1));
							circle.w(8);
							circle.h(8);
							nk_fill_circle(canvas, circle, nk_rgb(100, 100, 100, NkColor.mallocStack(stack)));
							if (nk_input_is_mouse_released(input, NK_BUTTON_LEFT) &&
									nk_input_is_mouse_hovering_rect(input, circle) &&
									linking.active == nk_true && linking.node != it) {
								linking.active = nk_false;
								node_editor_link(linking.input_slot, linking.node, i, it);
							}
						}
					}
				}

				if (linking.active == nk_true && nk_input_is_mouse_released(input, NK_BUTTON_LEFT)) {
					linking.active = nk_false;
					linking.node = null;
				}

				for (int i = 0; i < node_links.size(); i++) {
					EditorNodeLink link = node_links.get(i);
					EditorNode ni = node_editor_find(link.input_id);
					EditorNode no = node_editor_find(link.output_id);

					float spacei = node.bounds().h() / (float)((ni.outputCount) + 1);
					float spaceo = node.bounds().h() / (float)((no.inputCount) + 1);
					NkVec2 l0 = NkVec2.mallocStack(stack);
					l0 = nk_layout_space_to_screen(ctx, nk_vec2(ni.bounds.x() + ni.bounds.w(), 3.0f + ni.bounds.y() + spacei * (float)(link.input_slot + 1), l0));
					NkVec2 l1 = NkVec2.mallocStack(stack);
					l1 = nk_layout_space_to_screen(ctx, nk_vec2(no.bounds.x(), 3.0f + no.bounds.y() + spaceo * (float) (link.output_slot + 1), l1));

					l0.x(l0.x() - scroll.x);
					l0.y(l0.y() - scroll.y);
					l1.x(l1.x() - scroll.x);
					l1.y(l1.y() - scroll.y);
					
					nk_stroke_curve(canvas, l0.x(), l0.y(), l0.x() + 50.0f, l0.y(),
							l1.x() - 50.0f, l1.y(), l1.x(), l1.y(), 1.0f, nk_rgb(100, 100, 100, NkColor.mallocStack(stack)));
				}

				if (updated != null) {
					node_editor_pop(updated);
					node_editor_push(updated);
				}

				if (nk_input_mouse_clicked(input, NK_BUTTON_LEFT, nk_layout_space_bounds(ctx, total_space))) {
					EditorNode it = begin;
					selected = null;
					bounds = nk_rect(input.mouse().pos().x(), input.mouse().pos().y(), 100, 200, bounds);
					while (it != null) {
						NkRect b = nk_layout_space_rect_to_screen(ctx, bounds);
						if (nk_input_is_mouse_hovering_rect(input, b))
							selected = it;
						it = it.next;
					}
				}

			}
			nk_layout_space_end(ctx);
			
			
			if (nk_input_is_mouse_hovering_rect(input, nk_window_get_bounds(ctx, NkRect.mallocStack(stack))) &&
		            nk_input_is_mouse_down(input, NK_BUTTON_MIDDLE)) {
		            scroll.x -= input.mouse().delta().x();
		            scroll.y -= input.mouse().delta().y();
		        }

		}
		nk_end(ctx);
	}

	public void node_editor_push(EditorNode node) {
		if (begin == null) {
			node.next = null;
			node.next = null;
			begin = node;
			end = node;
		}
		else {
			node.prev = end;
			if (end != null) {
				end.next = node;
			}
			node.next = null;
			end = node;
		}
	}

	public void node_editor_pop(EditorNode node) {
		if (node.next != null) {
			node.next.prev = node.prev;
		}
		if (node.prev != null) {
			node.prev.next = node.next;
		}
		if (end == node) {
			end = node.prev;
		}
		if (begin == node) {
			begin = node.next;
		}
		node.next = null;
		node.prev = null;
	}

	public EditorNode node_editor_find(int id) {
		EditorNode iter = begin;
		while (iter != null) {
			if (iter.id == id)
				return iter;
			iter = iter.next;
		}
		return null;
	}

	public static void node_editor_add_node(NodeEditor editor, String name, Vector4 bounds, int in_count, int out_count) {
		EditorNode node = new EditorNode();
		node.id = editor.ids++;
		node.inputCount = in_count;
		node.outputCount = out_count;
		NkRect bds = NkRect.mallocStack();
		bds = nk_rect(bounds.x, bounds.y, bounds.z, bounds.w, bds);
		node.bounds = bds;
		node.name = new String(name);
		editor.node_buffer.add(node);
		editor.node_editor_push(node);
	}
	
	public static void node_editor_add_node_vector2(NodeEditor editor, String name, Vector4 bounds, int in_count, int out_count, Vector2 value) {
		Vector2Node node = new Vector2Node();
		node.value = value;
		node.id = editor.ids++;
		node.inputCount = in_count;
		node.outputCount = out_count;
		NkRect bds = NkRect.mallocStack();
		bds = nk_rect(bounds.x, bounds.y, bounds.z, bounds.w, bds);
		node.bounds = bds;
		node.name = new String(name);
		editor.node_buffer.add(node);
		editor.node_editor_push(node);
	}

	public static void node_editor_add_node_vector3(NodeEditor editor, String name, Vector4 bounds, int in_count, int out_count, Vector3 value) {
		Vector3Node node = new Vector3Node();
		node.value = value;
		node.id = editor.ids++;
		node.inputCount = in_count;
		node.outputCount = out_count;
		NkRect bds = NkRect.mallocStack();
		bds = nk_rect(bounds.x, bounds.y, bounds.z, bounds.w, bds);
		node.bounds = bds;
		node.name = new String(name);
		editor.node_buffer.add(node);
		editor.node_editor_push(node);
	}

	public static void node_editor_add_node_vector4(NodeEditor editor, String name, Vector4 bounds, int in_count, int out_count, Vector4 value) {
		Vector4Node node = new Vector4Node();
		node.value = value;
		node.id = editor.ids++;
		node.inputCount = in_count;
		node.outputCount = out_count;
		NkRect bds = NkRect.mallocStack();
		bds = nk_rect(bounds.x, bounds.y, bounds.z, bounds.w, bds);
		node.bounds = bds;
		node.name = new String(name);
		editor.node_buffer.add(node);
		editor.node_editor_push(node);
	}

	public void node_editor_link(int in_slot, EditorNode in, int out_slot, EditorNode out) {
		if (!in.addInput(in_slot, out)) {
			System.out.println(in_slot + " input");
			return;
		}
		if (!out.addOutput(out_slot, in)) {
			System.out.println(out_slot + " output");
			out.removeOutput(out_slot, in);
			return;
		}
		EditorNodeLink link = new EditorNodeLink();
		link.input_id = in.id;
		link.input_slot = in_slot;
		link.output_id = out.id;
		link.output_slot = out_slot;
		link.input = in;
		link.output = out;
		
		System.out.println("IN: " + in.name + "\tOUT: " + out.name);
		node_links.add(link);
	}

}
